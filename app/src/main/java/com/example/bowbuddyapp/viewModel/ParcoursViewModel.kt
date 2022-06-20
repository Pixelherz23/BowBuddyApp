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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.HttpException
import java.io.IOException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ParcoursViewModel @Inject constructor(private var api: ApiRequests, application: Application, var acct : GoogleSignInAccount): AndroidViewModel(application) {
    private val _parcours = MutableLiveData<List<Parcours>>()
    val parcours: LiveData<List<Parcours>> = _parcours

    private val _pbVisibility = MutableLiveData<Int>()
    val pbVisibility: LiveData<Int> = _pbVisibility

    private val linkLiveData = MutableLiveData<String>()
    val link : LiveData<String> = linkLiveData
    val game = MutableLiveData<Game>()

    private val _gameExists = MutableLiveData<Boolean>()
    val gameExists: LiveData<Boolean> = _gameExists

    val parcoursIdTodelete = MutableLiveData<String>()

    init {
        fetchData(acct.email.toString())
        generateLink()
    }

    fun generateLink(){
        val prefix = "https://bow-buddy.com/"
        var randomStr: String = UUID.randomUUID().toString()
        while (randomStr.length < 10) {
            randomStr += UUID.randomUUID().toString()
        }
        val link = randomStr.substring(0, 10)
        linkLiveData.value = prefix + link
    }

    fun fetchData(email: String){
        Log.i("PVM Before Fetch _Parcours",_parcours.value.toString() )
        Log.i("PVM Before Fetch Parcours",parcours.value.toString() )
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
                Log.e("PVM", "Request success")
                _parcours.value = response.body()!!


            }else if(response.code() == 404){
                _parcours.value = listOf()


            } else{
                Log.e("PVM", "Response not Successful ${response.code()}")

            }
            _pbVisibility.value = View.GONE

            Log.i("PVM After Fetch _Parcours",_parcours.value.toString() )
            Log.i("PVM After Fetch Parcours",parcours.value.toString() )
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
                Log.e("PVM", "Response not Successful: ${response.code()}")
                Toast.makeText(getApplication<Application>().applicationContext
                    , "Sending failed", Toast.LENGTH_SHORT).show()
            }
            // pbVisibilityLiveData.value = View.GONE
        }
    }

    fun fetchGame(link: String){
        viewModelScope.launch {
            val response = try{
                api.getGame(link)
            } catch(e: IOException){
                Log.e("GVM", "IOException, you might not have internet connection")
                return@launch
            } catch (e: HttpException){
                Log.e("GVM", "HttpException, unexpected response")
                return@launch
            }
            if(response.isSuccessful && response.body() != null) {
                _gameExists.value = true
            }else if(response.code() == 404){
                _gameExists.value = false
            }else{
                Log.e("GVM", "Response not Successful: ${response.code()}")
            }
        }
    }

    fun deleteParcours() : Job {

        var x = viewModelScope.launch() {
            val response = try{
               api.deleteParcours(parcoursIdTodelete.value!!)
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

        }

        return x
    }

    fun updateUser(email: String, link: String){
        viewModelScope.launch() {
            val response = try{
                api.updateUserGame(email, link)
            } catch(e: IOException){
                Log.e("PVM", "IOException, you might not have internet connection")
                return@launch
            } catch (e: HttpException){
                Log.e("PVM", "HttpException, unexpected response")
                return@launch
            }
            if(response.isSuccessful) {
                Toast.makeText(getApplication<Application>().applicationContext
                    , "Sending success", Toast.LENGTH_SHORT).show()
            }else{
                Log.e("PVM", "Response not Successful")
                Toast.makeText(getApplication<Application>().applicationContext
                    , "Sending failed", Toast.LENGTH_SHORT).show()
            }

        }
    }

}