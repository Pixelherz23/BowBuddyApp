package com.example.bowbuddyapp.viewModel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bowbuddyapp.api.requests.ApiRequests
import com.example.bowbuddyapp.data.Parcours
import com.example.bowbuddyapp.ui.game.GameActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.NullPointerException
import javax.inject.Inject
import com.example.bowbuddyapp.ui.CreateParcoursActivity

/**
 * ViewModel for the [CreateParcoursActivity].
 * This class is responsible for sending a new parcours to the database.
 *
 * @author Kai-U. Stieler
 * @property api provides the methods to make request to the server
 */
@HiltViewModel
class CreateParcoursViewModel @Inject constructor(private var api: ApiRequests): ViewModel() {

    var parcoursId = MutableLiveData<String>()
     val parcours= MutableLiveData<Parcours>()

    /**
     * Makes an api request in a coroutine to send the parcours
     */
    fun sendParcours() {
        viewModelScope.launch() {
            val response = try{
                //TODO Watch out, API Request is changed
                api.createParcours(parcours.value!!)
            } catch(e: IOException){
                Log.e("CPVM", "IOException, you might not have internet connection")
                Log.e("CPVM", e.toString())
                return@launch
            } catch (e: HttpException){
                Log.e("CPVM", "HttpException, unexpected response")
                return@launch
            }catch (e : NullPointerException) {
                Log.e("CPVM", "NullPointerException")
                return@launch
            }

            if(response.isSuccessful && response.body() != null) {
                //TODO Pass (parcours) id for the creation of stations

                    var id = response.body()?.string()
                parcoursId.value = id.toString()

                Log.i(" parcoursId.value",   parcoursId.value.toString())
                Log.i("Msg", "Sendning Parcours succuess")

            }else{
                Log.e("Msg", "Sending failed ${response.toString()}")
            }

        }
    }


    }