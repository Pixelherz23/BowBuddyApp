package com.example.bowbuddyapp.viewModel

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bowbuddyapp.api.requests.ApiRequests
import com.example.bowbuddyapp.data.Points
import com.example.bowbuddyapp.data.PointsStation
import com.example.bowbuddyapp.data.Station
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

    private val _pointsTargets = MutableLiveData<MutableList<Points>>()
    val pointsTargets: LiveData<MutableList<Points>> = _pointsTargets

    private val _pointsStation = MutableLiveData<PointsStation>()
    val pointsStation: LiveData<PointsStation> = _pointsStation

    private val _pointsParcours = MutableLiveData<Pair<Int, Int>>()
    val pointsParcours: LiveData<Pair<Int, Int>> = _pointsParcours

    private val _pbVisibility = MutableLiveData<Int>()
    val pbVisibility: LiveData<Int> = _pbVisibility

    fun fetchTargets(station: Int){
        viewModelScope.launch {
            _pbVisibility.value = View.VISIBLE
            val response = try{
                api.getTargets(station)
            } catch(e: IOException){
                Log.e("SVM", "IOException, you might not have internet connection")
                _pbVisibility.value = View.GONE
                return@launch
            } catch (e: HttpException){
                Log.e("SVM", "HttpException, unexpected response")
                _pbVisibility.value = View.GONE
                return@launch
            }

            if(response.isSuccessful && response.body() != null) {
                val targets = response.body()!!
                _targets.value = targets
                _pointsTargets.value = MutableList(targets.size){Points(0, "", 0, "")}

            }else{
                Log.e("SVM", "Response not Successful: ${response.code()}")
            }
            _pbVisibility.value = View.GONE
        }
    }

    fun fetchPoints(email: String, link: String, target: Int, position: Int){
        viewModelScope.launch {
            val response = try{
                api.getPointsTarget(email, target, link)
            } catch(e: IOException){
                Log.e("SVM", "IOException, you might not have internet connection")

                return@launch
            } catch (e: HttpException){
                Log.e("SVM", "HttpException, unexpected response")
                return@launch
            }
            if(response.isSuccessful && response.body() != null) {
                _pointsTargets.value?.set(position, response.body()!!)
                _pointsTargets.notifyObserver()
            }else{
                Log.e("SVM", "Response not Successful: ${response.code()}")
            }
        }
    }

    fun fetchPointsStation(email: String, link: String, station: Int){
        viewModelScope.launch {
            val response = try{
                api.getPointsStation(email, station, link)
            } catch(e: IOException){
                Log.e("SVM", "IOException, you might not have internet connection")

                return@launch
            } catch (e: HttpException){
                Log.e("SVM", "HttpException, unexpected response")
                return@launch
            }
            if(response.isSuccessful && response.body() != null) {
                _pointsStation.value = response.body()
            }else{
                Log.e("SVM", "Response not Successful: ${response.code()}")
            }
        }
    }


}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

