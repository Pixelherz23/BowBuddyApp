package com.example.bowbuddyapp.viewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import com.example.bowbuddyapp.api.requests.ApiRequests
import com.example.bowbuddyapp.data.Game
import com.example.bowbuddyapp.data.User
import com.example.bowbuddyapp.data.Parcours
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.example.bowbuddyapp.ui.main.HomeFragment
import retrofit2.HttpException
import java.io.IOException
import java.util.*
import javax.inject.Inject

/**
 * ViewModel for the [HomeFragment].
 * ParcoursViewModel is responsible for handling parcours related api requests and stores the parcours as LiveData.
 * This gives us the ability to observe the data. More info about MutableLiveData [see](https://developer.android.com/topic/libraries/architecture/livedata?authuser=1)
 *
 * @author Kai-U. Stieler, Lukas Beckmann (co. author)
 * @property api provides the methods to make request to the server
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

    private val _gameExists = MutableLiveData<Boolean>()
    val gameExists: LiveData<Boolean> = _gameExists

    val parcoursIdTodelete = MutableLiveData<String>()

    init {
        fetchData(acct.email.toString())
        generateLink()
    }

    /**
     * generates a link for a game, which is used to sync data between app and database
     */
    fun generateLink(){
        val prefix = "https://bow-buddy.com/"
        var randomStr: String = UUID.randomUUID().toString()
        while (randomStr.length < 10) {
            randomStr += UUID.randomUUID().toString()
        }
        val link = randomStr.substring(0, 10)
        linkLiveData.value = prefix + link
    }

    /**
     * Makes an api request in a coroutine to get the parcours and then stores it in the LiveData [_parcours]
     * @param email the email from the user for requesting [Parcours]
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
                _parcours.value = listOf()


            } else{
                Log.e("PVM", "Response not Successful ${response.code()}")

            }
            _pbVisibility.value = View.GONE

            Log.i("PVM After Fetch _Parcours",_parcours.value.toString() )
            Log.i("PVM After Fetch Parcours",parcours.value.toString() )
        }
    }

    /**
     * Makes an api request in a coroutine to send a new game
     * @param email the email from the user for assigning the created game to the user
     */
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
        }
    }

    /**
     * Makes an api request in a coroutine to check weather the game exist and than stores true or false in LiveData [_gameExists]
     * @param link the link for requesting the [Game]
     */
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


    /**
     * Makes an api request in a coroutine to delete a specific parcours.
     * The parcours will be deleted that has the same ID as found in parcoursIdTodelete
     * @return a [Job] to provide the ability to observe when the coroutine has finished
     */
    fun deleteParcours() : Job {

        val x = viewModelScope.launch() {
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

    /**
     * Makes an api request in a coroutine to assign a game to the user
     * @param email the email from the [user]
     * @param link the link from the [Game] for updating the user]
     */
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