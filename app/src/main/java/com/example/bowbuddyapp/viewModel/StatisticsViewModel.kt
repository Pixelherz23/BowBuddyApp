package com.example.bowbuddyapp.viewModel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bowbuddyapp.api.requests.ApiRequests
import com.example.bowbuddyapp.data.Game
import com.example.bowbuddyapp.data.Station
import com.example.bowbuddyapp.data.Statistics
import com.example.bowbuddyapp.data.User
import com.example.bowbuddyapp.data.Target
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
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
import com.example.bowbuddyapp.ui.main.StatisticsFragment

/**
 * ViewModel for the [StatisticsFragment]. The class stores the [statistics] as LiveData.
 * This gives us certain advantages. For more infos [see](https://developer.android.com/topic/libraries/architecture/livedata?authuser=1)
 * StatisticsViewModel is also responsible for handeling statistics related api requests
 *
 * @author Kai-U. Stieler
 * @property api provides the methods to make request to the server
 * @property acct the google account instance
 */
@HiltViewModel
class StatisticsViewModel @Inject constructor(private var api: ApiRequests, var acct: GoogleSignInAccount): ViewModel() {

    val statistics = MutableLiveData<Statistics>()

    private val pbVisibilityLiveData = MutableLiveData<Int>()
    val pbVisibility: LiveData<Int> = pbVisibilityLiveData

    init {
        getStatistics(acct.email.toString())
    }

    /**
     * Makes an api request in a coroutine to get the [Statistics] of a user and then stores it in the LiveData [statistics]
     * @param email the email from the [User] of the statistivs
     */
    fun getStatistics(email: String){
        viewModelScope.launch() {
            pbVisibilityLiveData.value = View.VISIBLE
            val response = try{
                api.getStatistics(email)
            } catch(e: IOException){
                Log.e("SVM", "IOException, you might not have internet connection")
                pbVisibilityLiveData.value = View.GONE
                return@launch
            } catch (e: HttpException){
                Log.e("SVM", "HttpException, unexpected response")
                pbVisibilityLiveData.value = View.GONE
                return@launch
            }
            if(response.isSuccessful && response.body() != null) {

                statistics.value =response.body()!!

            }else{
                Log.e("SVM", "Response not Successful")
            }
            pbVisibilityLiveData.value = View.GONE
        }
    }
}



