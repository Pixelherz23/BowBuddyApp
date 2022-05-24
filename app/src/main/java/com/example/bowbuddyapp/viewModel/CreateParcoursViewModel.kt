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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.NullPointerException
import javax.inject.Inject

@HiltViewModel
class CreateParcoursViewModel @Inject constructor(private var api: ApiRequests): ViewModel() {

     val parcours= MutableLiveData<Parcours>()
    //val parcours: LiveData<Parcours>

    //lateinit var parcours : Parcours


    fun sendParcours(){
        viewModelScope.launch() {

            val response = try{
                //TODO Watch out, API Request is changed
                api.createParcours(parcours.value!!)
            } catch(e: IOException){
                Log.e("PVM", "IOException, you might not have internet connection")

                return@launch
            } catch (e: HttpException){
                Log.e("PVM", "HttpException, unexpected response")
                return@launch
            }catch (e : NullPointerException) {
                Log.e("PVM", "NullPointerException")
                return@launch
            }

            if(response.isSuccessful && response.body() != null) {
                //TODO Pass (parcours) id for the creation of stations
                var id = response.body()
                Log.i("Msg", "Sendning Parcours succuess")
            }else{
                Log.e("Msg", "Sending failed ${response.toString()}")
            }
        }
    }

    }