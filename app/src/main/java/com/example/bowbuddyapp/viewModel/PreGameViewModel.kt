package com.example.bowbuddyapp.viewModel

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import com.example.bowbuddyapp.api.requests.ApiRequests
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.appsflyer.share.LinkGenerator
import com.example.bowbuddyapp.data.Game
import dagger.hilt.android.internal.Contexts.getApplication
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

@HiltViewModel
class PreGameViewModel @Inject constructor(private var api: ApiRequests, application: Application): AndroidViewModel(application) {

    private val linkLiveData = MutableLiveData<String>()
    val link : LiveData<String> = linkLiveData
    val game = MutableLiveData<Game>()

    fun generateLink(){
        linkLiveData.value = "https://bowbuddy.com7sTATic"

    }

    init {
        generateLink()
    }


    fun sendGame(){
        viewModelScope.launch() {

            val response = try{
                api.createGame(game.value!!)
            } catch(e: IOException){
                Log.e("PVM", "IOException, you might not have internet connection")
               // pbVisibilityLiveData.value = View.GONE
                return@launch
            } catch (e: HttpException){
                Log.e("PVM", "HttpException, unexpected response")
               // pbVisibilityLiveData.value = View.GONE
                return@launch
            }
            if(response.isSuccessful && response.body() != null) {

                //parcoursLiveData.value = response.body()!!
                Toast.makeText(getApplication<Application>().applicationContext
                    , "Sending success", Toast.LENGTH_SHORT).show()
            }else{
                Log.e("PVM", "Response not Successful")
                Toast.makeText(getApplication<Application>().applicationContext
                    , "Sending failed", Toast.LENGTH_SHORT).show()
            }
           // pbVisibilityLiveData.value = View.GONE
        }
    }
    }




