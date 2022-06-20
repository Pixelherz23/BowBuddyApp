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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.FieldPosition
import javax.inject.Inject

//TODO Documentation needed
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

    fun fetchAllPoints(link: String, email: String){
        if(targets.value != null){
            targets.value!!.forEachIndexed { index, target ->
                fetchPoints(email, link, target.id, index).toString()
            }
        }
    }

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

    fun fetchAllPoints(link: String, stationId: Int){
        if(player.value != null){
            player.value!!.forEachIndexed { index, player ->
                fetchPointsStation(player.email, link, stationId, index)
            }
        }
    }




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


}
fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

