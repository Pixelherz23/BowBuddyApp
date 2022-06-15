package com.example.bowbuddyapp.viewModel

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bowbuddyapp.api.requests.ApiRequests
import com.example.bowbuddyapp.data.Game
import com.example.bowbuddyapp.data.Station
import com.example.bowbuddyapp.data.Target
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(private var api: ApiRequests, application: Application): AndroidViewModel(application) {
    private val _game = MutableLiveData<Game>()
    val game: LiveData<Game> = _game

    private val _stations = MutableLiveData<List<Station>>()
    val stations: LiveData<List<Station>> = _stations

    private val _targets = ArrayList<List<Target>>()
    val targets: ArrayList<List<Target>> = _targets

    private val _pbVisibility = MutableLiveData<Int>()
    val pbVisibility: LiveData<Int> = _pbVisibility

    init {

    }

    fun fetchGame(link: String){
        viewModelScope.launch {
            _pbVisibility.value = View.VISIBLE
            val response = try{
                api.getGame(link)
            } catch(e: IOException){
                Log.e("GVM", "IOException, you might not have internet connection")
                _pbVisibility.value = View.GONE
                return@launch
            } catch (e: HttpException){
                Log.e("GVM", "HttpException, unexpected response")
                _pbVisibility.value = View.GONE
                return@launch
            }
            if(response.isSuccessful && response.body() != null) {

                _game.value = response.body()!!

            }else{
                Log.e("GVM", "Response not Successful")
            }
            _pbVisibility.value = View.GONE
        }
    }

    fun fetchStations(parcour: Int){
        viewModelScope.launch {
            _pbVisibility.value = View.VISIBLE
            val response = try{
                api.getStations(parcour)
            } catch(e: IOException){
                Log.e("GVM", "IOException, you might not have internet connection")
                _pbVisibility.value = View.GONE
                return@launch
            } catch (e: HttpException){
                Log.e("GVM", "HttpException, unexpected response")
                _pbVisibility.value = View.GONE
                return@launch
            }

            if(response.isSuccessful && response.body() != null) {
                val targets = ArrayList<List<Target>>()
                response.body()!!.forEach {
                    fetchTargets(it.id).await()
                }
                _stations.value = response.body()!!

            }else{
                Log.e("GVM", "Response not Successful")
            }
            _pbVisibility.value = View.GONE
        }
    }

    private suspend fun fetchTargets(station: Int) = viewModelScope.async {
        val response = try{
            api.getTarget(station)
        } catch(e: IOException){
            Log.e("PVM", "IOException, you might not have internet connection")
            _pbVisibility.value = View.GONE
            return@async
        } catch (e: HttpException){
            Log.e("PVM", "HttpException, unexpected response")
            _pbVisibility.value = View.GONE
            return@async
        }
        if(response.isSuccessful && response.body() != null) {
            _targets.add(response.body()!!)
        }else{
            Log.e("PVM", "Response not Successful")
        }
    }
}