package com.example.bowbuddyapp.viewModel;

import android.app.Application;
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import com.example.bowbuddyapp.api.requests.ApiRequests;
import com.example.bowbuddyapp.data.Game
import com.example.bowbuddyapp.data.PointsParcours
import com.example.bowbuddyapp.data.Statistics
import com.example.bowbuddyapp.data.User
import com.example.bowbuddyapp.data.Parcours
import com.example.bowbuddyapp.ui.game.ResultFragment
import com.example.bowbuddyapp.ui.main.HomeFragment

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.http.GET
import java.io.IOException


/**
 * ViewModel for the [ResultFragment].
 * This class contains the logic for calculating, fetching, and sending data which will be used to display the data in the ResultFragment
 * In order to do that the data is stored in LiveData.
 * This gives us the ability to observe the data. More info about MutableLiveData [see](https://developer.android.com/topic/libraries/architecture/livedata?authuser=1)
 *
 * @author Lukas Beckmann
 * @property api provides the methods to make request to the server
 */
@HiltViewModel
class ResultViewModel @Inject constructor(private var api: ApiRequests, application: Application): AndroidViewModel(application) {
    val MAX_POINTS_DSB = 11
    val MAX_POINTS_DFBV = 20

    private val _players = MutableLiveData<List<User>>()
    val players: LiveData<List<User>> = _players

    private val _pointsParcours = MutableLiveData<MutableList<PointsParcours>>()
    val pointsParcours: LiveData<MutableList<PointsParcours>> = _pointsParcours

    private val _hits = MutableLiveData<MutableMap<String, Int>>()
    val hits: LiveData<MutableMap<String, Int>> = _hits

    private val _maxTargets = MutableLiveData<Int>()
    val maxTargets: LiveData<Int> = _maxTargets

    private val _statistics = MutableLiveData<MutableMap<String, Statistics>>()
    val statistics: LiveData<MutableMap<String, Statistics>> = _statistics


    init {
        _hits.value = mutableMapOf()
        _statistics.value = mutableMapOf()
    }

    /**
     * calculates the maximum of points a user can score
     * @param rule the rule of the [Game] to get the maxPoints for one arrow
     * @param arrows the number of arrows
     */
    fun getMaxPoints(rule: String ,arrows: Int) =
        if(rule == "DSB (WA)") arrows*MAX_POINTS_DSB else arrows*MAX_POINTS_DFBV


    /**
     * calculates the hit probability based on the [hits]
     * @param arrows the number of arrows
     * @param email the email in order to get the hits for a user
     * @return the hit probability
     */
    fun getHitProbability(arrows: Int, email: String): Double{
        val hits = hits.value?.get(email)
        return if (hits != null) hits/arrows.toDouble() * 100 else 0.0
    }

    /**
     * calculates the statistics of a user by using the old statistics
     * @param email the email from the [User]
     * @param rule the rule from the [Game]
     * @param statistics the old [Statistics] from a user
     * @param multiplayer the flag to get weather a game is multiplayer or singleplayer
     * @return the new calculated [Statistics]
     */
    fun getNewStatistics(email: String, rule: String, statistics: Statistics, multiplayer: Boolean): Statistics{
        val hitProb = if(statistics.hitProbability == 0) getHitProbability(getMaxHits(rule)!!, email).toInt()
                    else ((statistics.hitProbability + getHitProbability(getMaxHits(rule)!!, email))/2).toInt()
        if(!multiplayer){
            return Statistics(hitProb, -1, -1, -1)
        }
        var position = -1
        players.value?.forEachIndexed {index, user ->
            if(email == user.email){
                position = index
            }
        }
        return when (position){
            0 -> Statistics(hitProb, statistics.placementFirst+1, -1, -1)
            1 -> Statistics(hitProb, -1, statistics.placementSecond+1, -1)
            2 -> Statistics(hitProb, -1, -1, statistics.placementThird-1)
            else -> Statistics(hitProb, -1, -1, -1)
        }
    }

    /**
     * calculates the maximum of hits/arrows a user as based on the [rule]
     * @return the calculated hits
     */
    fun getMaxHits(rule: String) =
        if(rule == "DSB (WA)") maxTargets.value?.times(2) else maxTargets.value

    /**
     * Makes an api request in a coroutine to get the hits from a user in the game and stores them in LiveData [_hits]
     * @param email the email from the [User]
     * @param link the link from the [Game]
     * @param parcours the id from the [Parcours]
     */
    private fun fetchHits(email: String, link: String, parcours: Int){
        viewModelScope.launch {
            val response = try {
                api.getHits(email, parcours, link)
            } catch (e: IOException) {
                Log.e("fetchHits", "IOException, you might not have internet connection")

                return@launch
            } catch (e: HttpException) {
                Log.e("fetchHits", "HttpException, unexpected response")
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                _hits.value?.put(email, response.body()!!)
            }else if(response.code() == 404){
                _hits.value?.put(email, 0)
            } else {
                Log.e("fetchHits", "Response not Successful: ${response.code()}")
            }
        }
    }

    /**
     * Makes an api request in a coroutine to get the number of targets from a parcours and stores them in LiveData [_maxTargets]
     * @param parcours the id from the [Parcours]
     */
    fun fetchMaxTargets(parcoursId: Int){
        viewModelScope.launch {
            val response = try {
                api.getMaxTargets(parcoursId)
            } catch (e: IOException) {
                Log.e("fetchMaxTargets", "IOException, you might not have internet connection")

                return@launch
            } catch (e: HttpException) {
                Log.e("fetchMaxTargets", "HttpException, unexpected response")
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                _maxTargets.value = response.body()
            } else {
                Log.e("fetchMaxTargets", "Response not Successful: ${response.code()}")
            }
        }
    }

    /**
     * Iterates over [players] and calls [fetchHits] for all
     * @param link the link from the [Game]
     * @param parcours the id from the [Parcours]
     */
    fun fetchAllHits(link: String, parcourId: Int){
        if(players.value != null){
            players.value!!.forEach {player ->
                fetchHits(player.email, link, parcourId)
            }
            _hits.notifyObserver()
        }
    }

    /**
     * Makes an api request in a coroutine to get the points on a parcour from a user in a game and stores them in LiveData [_pointsParcours]
     * @param email the email from the [User]
     * @param link the link from the [Game]
     * @param parcours the id from the [Parcours]
     */
    private fun fetchPointsParcours(email: String, link: String, parcours: Int){
        viewModelScope.launch {
            val response = try {
                api.getPointsParcours(email, parcours, link)
            } catch (e: IOException) {
                Log.e("fetchPointsParcours", "IOException, you might not have internet connection")

                return@launch
            } catch (e: HttpException) {
                Log.e("fetchPointsParcours", "HttpException, unexpected response")
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                var pos = 0
                pointsParcours.value?.forEachIndexed { index, points ->
                    if (points.email == email && points.link == link) {
                        pos = index
                    }
                }
                _pointsParcours.value!![pos] = response.body()!!
                _pointsParcours.notifyObserver()
            } else {
                Log.e("fetchPointsParcours", "Response not Successful: ${response.code()}")
            }
        }
    }

    /**
     * Iterates over [players] and calls [fetchPointsParcours] for all
     * @param link the link from the [Game]
     * @param parcours the id from the [Parcours]
     */
    fun fetchAllPoints(link: String, parcourId: Int){
        if(players.value != null){
            players.value!!.forEach {player ->
                fetchPointsParcours(player.email, link, parcourId)
            }
        }
    }

    /**
     * Makes an api request in a coroutine to get users from a game and stores them in LiveData [_players]
     * @param link the link from the [Game] for requesting the [User]s
     */
    fun fetchUser(link: String){
        viewModelScope.launch {
            val response = try {
                api.getUserGame(link)
            } catch (e: IOException) {
                Log.e("fetchUser", "IOException, you might not have internet connection ")

                return@launch
            } catch (e: HttpException) {
                Log.e("fetchUser", "HttpException, unexpected response")
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                _players.value = user
                _pointsParcours.value = MutableList(user.size){PointsParcours(0, "", 0, "")}
                user.forEachIndexed { index, user ->
                    _pointsParcours.value!![index] = PointsParcours(0, link, 0, user.email)
                }
            } else {
                Log.e("fetchUser", "Response not Successful: ${response.code()}")
            }
        }
    }

    /**
     * Makes an api request in a coroutine to delete a game
     * @param link the link from the [Game] to delete
     */
    fun deleteGame(link: String){
        viewModelScope.launch {
            val response = try {
                api.deleteGame(link)
            } catch (e: IOException) {
                Log.e("fetchUser", "IOException, you might not have internet connection")

                return@launch
            } catch (e: HttpException) {
                Log.e("fetchUser", "HttpException, unexpected response")
                return@launch
            }
            if (!response.isSuccessful) {
                Log.e("fetchUser", "Response not Successful: ${response.code()}")
            }
        }

    }

    /**
     * Makes an api request in a coroutine to get the statistics from a user and stores them in LiveData [_statistics]
     * @param email the email from the [User]
     */
    fun fetchStatistics(email: String){
        viewModelScope.launch {
            val response = try {
                api.getStatistics(email)
            } catch (e: IOException) {
                Log.e("fetchStatistics", "IOException, you might not have internet connection")

                return@launch
            } catch (e: HttpException) {
                Log.e("fetchStatistics", "HttpException, unexpected response")
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                _statistics.value?.put(email, response.body()!!)
                _statistics.notifyObserver()
                Log.e("fetchStatistics", "Response Successful: ${response}")
            } else {
                Log.e("fetchStatistics", "Response not Successful: ${response.code()}")
            }
        }
    }

    /**
     * Makes an api request in a coroutine to update the statistics from a user
     * @param email the email from the [User]
     * @param statistics the [Statistics] for updating the users statistics
     */
    fun updateStatistics(email: String, statistics: Statistics) {
        viewModelScope.launch {
            val response = try {
                api.updateStatistics(email, statistics)
            } catch (e: IOException) {
                Log.e("updateStatistics", "IOException, you might not have internet connection ${e}")

                return@launch
            } catch (e: HttpException) {
                Log.e("updateStatistics", "HttpException, unexpected response")
                return@launch
            }
            if (!response.isSuccessful) {
                Log.e("updateStatistics", "Response not Successful: ${response.code()}")
            }else{
                Log.e("updateStatistics", "Response not Successful: ${response}")
            }
        }
    }
}
