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
@HiltViewModel
class StationSetupViewModel @Inject constructor(private var api: ApiRequests): ViewModel() {

    private val _target = MutableLiveData<MutableList<Target>>()
    val target: LiveData<MutableList<Target>> = _target



    //private val _stationName = MutableLiveData<String>()
    //val stationName : MutableLiveData<String> = _stationName

    private val _stationName = MutableLiveData<String>()

     val _stationID = MutableLiveData<String>()
    //val stationID : MutableLiveData<String> = _stationID

    init {
        _target.value = mutableListOf<Target>()
        //_stationID.value = ""
        _stationName.value = ""

    }
    fun clear(){
       _target.value!!.clear()
        Log.i("clearing, _target",  _target.value.toString())
        Log.i("clearing, target", target.value.toString())

    }


    fun setStationID(id : String){
        _stationID.value = id
    }
    fun setStationName(name : String){
        _stationName.value = name
        Log.i("StationName", _stationName.value.toString() )
    }

    fun addTarget(target: com.example.bowbuddyapp.data.Target){

        if(target != null){
            _target.value!!.add(target)
        }

        //targetList.add(target)
        //targets.value = targetList

        /*
        Log.i("TArget", target.toString())
        //targetLiveData.value?.add(target)
        targetLiveData.value?.add(target)
        Log.i("ModelView", targets.value?.toString()!!)

         */


    }


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
     fun sendEachTargetToServer(){
        for (tempTarget in _target.value!!) {
                 viewModelScope.launch() {
                    Log.i("SVM_Target", "Coroutine executed")

                    val response = try {
                        Log.i("SVM_Target", "Sending target $tempTarget to  ${_stationID.value}")
                        api.createTarget(buildRequestBodyTarget(tempTarget, _stationID.value!!))

                        //var body = buildRequestBody(tempTarget, stationName)

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













