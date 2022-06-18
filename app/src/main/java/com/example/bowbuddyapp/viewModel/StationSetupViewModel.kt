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
import com.example.bowbuddyapp.data.Station
import com.example.bowbuddyapp.data.Target
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.lang.NullPointerException
import javax.inject.Inject


//TODO more logic form StationSetup to ViewMOdel (e.g Landscape mode deletes the input of user)

/**
 * ViewModel for holding data and sending it, when Stations and Targets are created
 * @author Kai-U. Stieler
 */
@HiltViewModel
class StationSetupViewModel @Inject constructor(private var api: ApiRequests): ViewModel() {

    private val _target = MutableLiveData<MutableList<Target>>()
    val target: LiveData<MutableList<Target>> = _target

    private val _stationName = MutableLiveData<String>()

     val _stationID = MutableLiveData<String>()

    init {
        _target.value = mutableListOf<Target>()
        _stationName.value = ""

    }
    fun clear(){
       _target.value!!.clear()
        Log.i("clearing, _target",  _target.value.toString())
        Log.i("clearing, target", target.value.toString())

    }

    fun setStationName(name : String){
        _stationName.value = name
        Log.i("StationName", _stationName.value.toString() )
    }

    fun addTarget(target: com.example.bowbuddyapp.data.Target){

        if(target != null){
            _target.value!!.add(target)
        }
    }

    /**
     * sending name of the station and the fitting parcoursID to server
     */
     fun sendStationToServer(parcoursId: String):Job {
           val x = viewModelScope.launch() {
               Log.i("SVM_station", "Coroutine executed")

               val response = try {
                   api.createStation(buildRequestBodyStation(_stationName.value!!, parcoursId))


               } catch (e: IOException) {
                   Log.e("SVM_station", "IOException, you might not have internet connection")

                   return@launch
               } catch (e: HttpException) {
                   Log.e("SVM_station", "HttpException, unexpected response")

                   return@launch
               }
               if (response.isSuccessful && response.body() != null) {
                   Log.i("SVM_station", "Response Successful")

                   var id = response.body()?.string().toString()

                   //TODO because server probably returnin station with \n

                   _stationID.value = id
                   Log.i("SVM_station", id)

               } else {
                   Log.e("SVM_station", "Response not Successful")

               }

           }
         return x
        }


    //TODO stop user to go on next page if sending unsuccessful
    /**
     * sending every Target to server
     */
     fun sendEachTargetToServer(){
        for (tempTarget in _target.value!!) {
                 viewModelScope.launch() {
                    Log.i("SVM_Target", "Coroutine executed")

                    val response = try {
                        Log.i("SVM_Target", "Sending target $tempTarget to  ${_stationID.value}")
                        api.createTarget(buildRequestBodyTarget(tempTarget, _stationID.value!!))
                    } catch (e: IOException) {
                        Log.e("SVM_Target", "IOException, you might not have internet connection")
                        // pbVisibilityLiveData.value = View.GONE
                        return@launch
                    } catch (e: HttpException) {
                        Log.e("SVM_Target", "HttpException, unexpected response")
                        // pbVisibilityLiveData.value = View.GONE
                        return@launch
                    }
                    if (response.isSuccessful && response.body() != null) {
                        Log.i("SVM_Target", "Response Successful")
                        _target.value!!.remove(tempTarget)
                    } else {
                        Log.e("SVM_Target", "Response not Successful ${response.body()}")

                    }

                }

            }
    }

    /**
     * Method for building a request body for sending a target
     *
     * @param target target which meant to be send
     * @param stationID corresponding stationId
     * @return the request body
     */
    fun buildRequestBodyTarget(target: Target, stationID: String): RequestBody {
        val jsonString = Gson().toJson(target)
        var jsonObj = JSONObject(jsonString)
        jsonObj.put("Station_idStation", stationID)

        val mediaType = "application/json; charset=utf-8".toMediaType()

        val requestBody = jsonObj.toString().toRequestBody(mediaType)
       //TODO for some reason there is a \n in idStation
        Log.i("body", jsonObj.toString())
        return requestBody
    }
    /**
     * Method for building a request body for sending a target
     *
     * @param station station which meant to be send
     * @param parcoursId corresponding parcoursId
     * @return the request body
     */
    fun buildRequestBodyStation(station: String, parcoursId: String): RequestBody {

        var jsonObj = JSONObject()
        //jsonObj.put("Station_idStation", station)
        jsonObj.put("nameStation", station)
        jsonObj.put("Parcours_idParcours", parcoursId)


        val mediaType = "application/json; charset=utf-8".toMediaType()

        val requestBody = jsonObj.toString().toRequestBody(mediaType)
        //Log.i("body", jsonObj.toString())
        return requestBody
    }


    }













