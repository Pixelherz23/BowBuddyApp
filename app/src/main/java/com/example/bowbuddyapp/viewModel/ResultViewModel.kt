package com.example.bowbuddyapp.viewModel;

import android.app.Application;
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import com.example.bowbuddyapp.api.requests.ApiRequests;
import com.example.bowbuddyapp.data.PointsParcours
import com.example.bowbuddyapp.data.Statistics
import com.example.bowbuddyapp.data.User

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

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
    fun getMaxPoints(rule: String ,arrows: Int) =
        if(rule == "DSB (WA)") arrows*MAX_POINTS_DSB else arrows*MAX_POINTS_DFBV

    fun getHitProbability(arrows: Int, email: String): Double{
        val hits = hits.value?.get(email)
        return if (hits != null) hits/arrows.toDouble() * 100 else 0.0
    }

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

    fun getMaxHits(rule: String) =
        if(rule == "DSB (WA)") maxTargets.value?.times(2) else maxTargets.value

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

    fun fetchAllHits(link: String, parcourId: Int){
        if(players.value != null){
            players.value!!.forEach {player ->
                fetchHits(player.email, link, parcourId)
            }
            _hits.notifyObserver()
        }
    }

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

    fun fetchAllPoints(link: String, parcourId: Int){
        if(players.value != null){
            players.value!!.forEach {player ->
                fetchPointsParcours(player.email, link, parcourId)
            }
        }
    }

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
