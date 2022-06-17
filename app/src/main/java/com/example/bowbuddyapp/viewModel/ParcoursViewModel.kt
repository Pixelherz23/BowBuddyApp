package com.example.bowbuddyapp.viewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import com.example.bowbuddyapp.api.requests.ApiRequests
import com.example.bowbuddyapp.data.Game
import com.example.bowbuddyapp.data.Parcours
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ParcoursViewModel @Inject constructor(private var api: ApiRequests, application: Application): AndroidViewModel(application) {
    private val _parcours = MutableLiveData<List<Parcours>>()
    val parcours: LiveData<List<Parcours>> = _parcours

    private val _pbVisibility = MutableLiveData<Int>()
    val pbVisibility: LiveData<Int> = _pbVisibility

    private val linkLiveData = MutableLiveData<String>()
    val link : LiveData<String> = linkLiveData
    val game = MutableLiveData<Game>()

    init {
        //TODO change this static implementation
        fetchData("test@api.com")
    }

    fun generateLink(){
        val prefix = "https://bowbuddy.com/"
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val link = List(10){ charset.random() }.joinToString("")
        linkLiveData.value = prefix + link

    }

    fun fetchData(email: String){
        viewModelScope.launch() {
            _pbVisibility.value = View.VISIBLE
            val response = try{
                api.getParcours(email)
            } catch(e: IOException){
                Log.e("PVM", "IOException, you might not have internet connection")
                _pbVisibility.value = View.GONE
                return@launch
            } catch (e: HttpException){
                Log.e("PVM", "HttpException, unexpected response")
                _pbVisibility.value = View.GONE
                return@launch
            }
            if(response.isSuccessful && response.body() != null) {

                _parcours.value = response.body()!!

            }else{
                Log.e("PVM", "Response not Successful")
            }
            _pbVisibility.value = View.GONE
        }
    }

    fun sendGame(email: String){
        viewModelScope.launch() {

            val response = try{
                api.createGame(email, game.value!!)
            } catch(e: IOException){
                Log.e("PVM", "IOException, you might not have internet connection")
                return@launch
            } catch (e: HttpException){
                Log.e("PVM", "HttpException, unexpected response")
                return@launch
            }
            if(response.isSuccessful && response.body() != null) {
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