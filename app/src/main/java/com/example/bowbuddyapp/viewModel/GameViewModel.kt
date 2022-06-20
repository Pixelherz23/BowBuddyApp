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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.example.bowbuddyapp.ui.game.GameActivity
import com.example.bowbuddyapp.ui.main.HomeFragment
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


/**
 * ViewModel for the [GameActivity].
 * This class is responsible for getting the game and stations and stores them as LiveData.
 * This gives us the ability to observe the data. More info about MutableLiveData [see](https://developer.android.com/topic/libraries/architecture/livedata?authuser=1)
 *
 * @author Lukas Beckmann
 * @property api provides the methods to make request to the server
 */
@HiltViewModel
class GameViewModel @Inject constructor(private var api: ApiRequests, application: Application): AndroidViewModel(application) {
    private val _game = MutableLiveData<Game>()
    val game: LiveData<Game> = _game

    private val _stations = MutableLiveData<List<Station>>()
    val stations: LiveData<List<Station>> = _stations

    private val _pbVisibility = MutableLiveData<Int>()
    val pbVisibility: LiveData<Int> = _pbVisibility


    /**
     * Makes an api request in a coroutine to get a game and then stores it in the LiveData [_game]
     * @param link the link for requesting the [Game]
     */
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
                Log.e("GVM", "Response not Successful: ${response.code()}")

            }
            _pbVisibility.value = View.GONE
        }
    }

    /**
     * Makes an api request in a coroutine to get the stations and then stores it in the LiveData [_stations]
     * @param parcour the id of the parcour for requesting the [Station]s
     */
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
                _stations.value = response.body()!!

            }else{
                Log.e("GVM", "Response not Successful: ${response.code()}")
            }
            _pbVisibility.value = View.GONE
        }
    }


}