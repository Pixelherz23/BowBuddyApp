package com.example.bowbuddyapp.viewModel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bowbuddyapp.api.requests.ApiRequests
import com.example.bowbuddyapp.data.Game
import com.example.bowbuddyapp.data.Statistics
import com.example.bowbuddyapp.data.Target
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(private var api: ApiRequests): ViewModel() {

    val statistics = MutableLiveData<Statistics>()

    private val pbVisibilityLiveData = MutableLiveData<Int>()
    val pbVisibility: LiveData<Int> = pbVisibilityLiveData




    init {
        //TODO change this static implementation
        getStatistics("test@api.com")
    }


    fun getStatistics(email: String){
        viewModelScope.launch() {
            pbVisibilityLiveData.value = View.VISIBLE
            val response = try{
                api.getStatistics(email)
            } catch(e: IOException){
                Log.e("PVM", "IOException, you might not have internet connection")
                pbVisibilityLiveData.value = View.GONE
                return@launch
            } catch (e: HttpException){
                Log.e("PVM", "HttpException, unexpected response")
                pbVisibilityLiveData.value = View.GONE
                return@launch
            }
            if(response.isSuccessful && response.body() != null) {

                statistics.value =response.body()!!

            }else{
                Log.e("PVM", "Response not Successful")
            }
            pbVisibilityLiveData.value = View.GONE
        }
    }
}



