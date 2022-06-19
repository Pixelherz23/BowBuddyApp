package com.example.bowbuddyapp.viewModel;

import android.app.Application;
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import com.example.bowbuddyapp.api.requests.ApiRequests;
import com.example.bowbuddyapp.data.PointsStation
import com.example.bowbuddyapp.data.User

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@HiltViewModel
class ResultViewModel @Inject constructor(private var api: ApiRequests, application: Application): AndroidViewModel(application) {
    val MAX_POINTS_DSB = 11
    val MAX_POINTS_DFBV = 20

    private val _player = MutableLiveData<List<User>>()
    val player: LiveData<List<User>> = _player

    private val _hits = MutableLiveData<Int>()
    val hits: LiveData<Int> = _hits

    fun getMaxPoints(rule: String , targets: Int) =
        if(rule == "DSB (WA)") targets*2*MAX_POINTS_DSB else targets*MAX_POINTS_DFBV

    fun getHitProbability(arrows: Int) = if(_hits.value != null) hits.value?.div(arrows)?.times(100) else 0

    fun fetchHits(email: String, link: String, parcours: Int){
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
                _hits.value = response.body()!!
            } else {
                Log.e("fetchHits", "Response not Successful: ${response.code()}")
            }
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
            } else {
                Log.e("fetchUser", "Response not Successful: ${response.code()}")
            }
        }
    }


}
