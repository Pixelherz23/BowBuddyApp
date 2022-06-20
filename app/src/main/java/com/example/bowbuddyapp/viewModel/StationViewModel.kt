package com.example.bowbuddyapp.viewModel

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bowbuddyapp.api.requests.ApiRequests
import com.example.bowbuddyapp.data.*
import com.example.bowbuddyapp.data.Target
import com.example.bowbuddyapp.ui.game.ResultFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.FieldPosition
import javax.inject.Inject
import com.example.bowbuddyapp.ui.game.StationFragment

/**
 * ViewModel for the [StationFragment].
 * This class contains the logic for getting the targets of a station and updating the points
 * In order to do that the data is stored in LiveData.
 * This gives us the ability to observe the data. More info about MutableLiveData [see](https://developer.android.com/topic/libraries/architecture/livedata?authuser=1)
 *
 * @author Lukas Beckmann
 * @property api provides the methods to make request to the server
 */
@HiltViewModel
class StationViewModel @Inject constructor(private var api: ApiRequests, application: Application): AndroidViewModel(application) {

    private val _targets = MutableLiveData<List<Target>>()
    val targets: LiveData<List<Target>> = _targets

    private val _player = MutableLiveData<List<User>>()
    val player: LiveData<List<User>> = _player

    private val _pointsTargets = MutableLiveData<MutableList<Points>>()
    val pointsTargets: LiveData<MutableList<Points>> = _pointsTargets

    private val _pointsStation = MutableLiveData<MutableList<PointsStation>>()
    val pointsStation: LiveData<MutableList<PointsStation>> = _pointsStation

    private val _pbVisibility = MutableLiveData<Int>()
    val pbVisibility: LiveData<Int> = _pbVisibility


    /**
     * Makes an api request in a coroutine to get the targets and then stores it in the LiveData [_targets]
     * @param station the id from the [Station] to request the [Target]s
     */
    fun fetchTargets(station: Int) {
        viewModelScope.launch {
            _pbVisibility.value = View.VISIBLE
            val response = try {
                api.getTargets(station)
            } catch (e: IOException) {
                Log.e("fetchTargets", "IOException, you might not have internet connection")
                _pbVisibility.value = View.GONE
                return@launch
            } catch (e: HttpException) {
                Log.e("fetchTargets", "HttpException, unexpected response")
                _pbVisibility.value = View.GONE
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                val targets = response.body()!!
                _targets.value = targets
                _pointsTargets.value = MutableList(targets.size) { Points(0, "", 0, "")}

            } else {
                Log.e("fetchTargets", "Response not Successful: ${response.code()}")
            }
            _pbVisibility.value = View.GONE
        }
    }

    /**
     * Makes an api request in a coroutine to get users from a game and stores them in LiveData [_player]
     * @param link the link from the [Game] for requesting the [User]s
     */
    fun fetchUser(link: String){
        viewModelScope.launch {
            val response = try {
                api.getUserGame(link)
            } catch (e: IOException) {
                Log.e("fetchUser", "IOException, you might not have internet connection")

                return@launch
            } catch (e: HttpException) {
                Log.e("fetchUser", "HttpException, unexpected response")
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                _player.value = user
                _pointsStation.value = MutableList(user.size) {PointsStation(0, "", 0, "")}
            } else {
                Log.e("fetchUser", "Response not Successful: ${response.code()}")
            }
        }
    }


    /**
     * Makes an api request in a coroutine to get the [Points] on a target from a user in a game and stores them in LiveData [_pointsTargets]
     * @param email the email from the [User]
     * @param link the link from the [Game]
     * @param target the id from the [Target]
     * @param position the position for the [_pointsStation]
     */
    private fun fetchPoints(email: String, link: String, target: Int, position: Int) {
        viewModelScope.launch {
            val response = try {
                api.getPointsTarget(email, target, link)
            } catch (e: IOException) {
                Log.e("fetchPoints", "IOException, you might not have internet connection")

                return@launch
            } catch (e: HttpException) {
                Log.e("fetchPoints", "HttpException, unexpected response")
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                _pointsTargets.value?.set(position, response.body()!!)
                _pointsTargets.notifyObserver()
            } else {
                Log.e("fetchPoints", "Response not Successful: ${response.code()}")
            }
        }
    }

    /**
     * Iterates over [targets] and calls [fetchPoints] for all
     * @param link the link from the [Game]
     * @param email the email from the [User]
     */
    fun fetchAllPoints(link: String, email: String){
        if(targets.value != null){
            targets.value!!.forEachIndexed { index, target ->
                fetchPoints(email, link, target.id, index).toString()
            }
        }
    }

    /**
     * Makes an api request in a coroutine to get the [PointsStation] on a station from a user in a game and stores them in LiveData [_pointsStation]
     * @param email the email from the [User]
     * @param link the link from the [Game]
     * @param station the id from the [Station]
     * @param position the position for the [_pointsStation]
     */
    private fun fetchPointsStation(email: String, link: String, station: Int, position: Int) {
        viewModelScope.launch {
            val response = try {
                api.getPointsStation(email, station, link)
            } catch (e: IOException) {
                Log.e("fetchPointsStation", "IOException, you might not have internet connection")

                return@launch
            } catch (e: HttpException) {
                Log.e("fetchPointsStation", "HttpException, unexpected response")
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                _pointsStation.value?.set(position, response.body()!!)
                _pointsStation.notifyObserver()
            } else {
                Log.e("fetchPointsStation", "Response not Successful: ${response.code()}")
            }
        }
    }


    /**
     * Iterates over [player] and calls [fetchPointsStation] for all
     * @param link the link from the [Game]
     * @param parcours the id from the [Parcours]
     */
    fun fetchAllPoints(link: String, stationId: Int){
        if(player.value != null){
            player.value!!.forEachIndexed { index, player ->
                fetchPointsStation(player.email, link, stationId, index)
            }
        }
    }




    /**
     * Makes an api request in a coroutine to update the points and call [fetchPoints] for automatically fetch the new data
     * @param points the [Points] to update
     * @param position the position in the List [pointsTargets] to update after sending
     */
    fun sendPoints(points: Points, position: Int) {
        viewModelScope.launch {
            val response = try {
                api.createPoints(points)
            } catch (e: IOException) {
                Log.e("sendPoints", "IOException, you might not have internet connection")
                return@launch
            } catch (e: HttpException) {
                Log.e("sendPoints", "HttpException, unexpected response")
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                Toast.makeText(getApplication<Application>().applicationContext
                    , "Sending success", Toast.LENGTH_SHORT).show()
                fetchPoints(points.email, points.link, points.target, position)
            } else {
                Log.e("sendPoints", "Response not Successful: ${response.code()}")
                Toast.makeText(getApplication<Application>().applicationContext
                    , "Sending failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getDsbValues() = arrayOf("11", "10", "8", "5", "0")
    fun getDfbvValues(arrow: Int) = arrayOf((24-arrow*4).toString(), (22-arrow*4).toString())


}

/**
 * reassign the LiveData to notify the observer.
 * This is usfull when e.g collections are used in liveData
 * @receiver [MutableLiveData]
 */
fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

