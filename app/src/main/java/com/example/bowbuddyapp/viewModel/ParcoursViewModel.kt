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
import javax.inject.Inject

/**
 * ViewModel for the HomeFragment. The class stores the parcours as LiveData.
 * This gives us the ability to observe the data which is helpful in the HomeFragment. More info about MutableLiveData [see](https://developer.android.com/topic/libraries/architecture/livedata?authuser=1)
 * ParcoursViewModel is also responsible for handeling parcours related api requests
 * @author Kai-U. Stieler, Lukas Beckmann (co. author)
 */
@HiltViewModel
class ParcoursViewModel @Inject constructor(private var api: ApiRequests, application: Application, var acct : GoogleSignInAccount): AndroidViewModel(application) {
    private val _parcours = MutableLiveData<List<Parcours>>()
    val parcours: LiveData<List<Parcours>> = _parcours

    private val _pbVisibility = MutableLiveData<Int>()
    val pbVisibility: LiveData<Int> = _pbVisibility

    private val linkLiveData = MutableLiveData<String>()
    val link : LiveData<String> = linkLiveData
    val game = MutableLiveData<Game>()

    val parcoursIdTodelete = MutableLiveData<String>()

    init {
        //TODO change this static implementation [DONE]
        fetchData(acct.email.toString())
    }

    fun generateLink(){
        val prefix = "https://bowbuddy.com/"
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val link = List(10){ charset.random() }.joinToString("")
        linkLiveData.value = prefix + link

    }

    /**
     * requests all Parcours related to given email
     * @param email the useremail
     */
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
                //_parcours.value?.clear()
                _parcours.value = listOf()


            } else{
                Log.e("PVM", "Response not Successful ${response.code()}")

            }
            _pbVisibility.value = View.GONE

            Log.i("PVM After Fetch _Parcours",_parcours.value.toString() )
            Log.i("PVM After Fetch Parcours",parcours.value.toString() )
        }
    }

    //TODO kann das weg?
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
    /**
     * request to delete a specific parcours. the parcours will be deleted that has the same ID as found in parcoursIdTodelete
     */
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

}