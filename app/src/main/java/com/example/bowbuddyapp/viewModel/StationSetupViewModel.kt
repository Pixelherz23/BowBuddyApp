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
import com.example.bowbuddyapp.data.Target
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.NullPointerException
import javax.inject.Inject


//TODO more logic form StationSetup to ViewMOdel (e.g Landscape mode deletes the input of user)
@HiltViewModel
class StationSetupViewModel @Inject constructor(private var api: ApiRequests): ViewModel() {
    //private var targetLiveData = MutableLiveData<MutableList<com.example.bowbuddyapp.data.Target>>()

    //private var targetLiveData = MutableLiveData<List<Target>>()
    //val targets: LiveData<List<Target>> = targetLiveData

    //V2
    private val targetList = mutableListOf<Target>()
    private val  targets = MutableLiveData<List<Target>>()

    //private val _stationName = MutableLiveData<String>()
    //val stationName : LiveData<String> = _stationName

    fun sendTargets() {

    }
    init {
        targets.value = targetList
    }


    fun addTarget(target: com.example.bowbuddyapp.data.Target){
       //V2
        targetList.add(target)
        targets.value = targetList
        Log.i("TArget", targets.value.toString())


        /*
        Log.i("TArget", target.toString())
        //targetLiveData.value?.add(target)
        targetLiveData.value?.add(target)
        Log.i("ModelView", targets.value?.toString()!!)

         */


    }

    fun sendEachTargetToServer(stationName: String){
        /*
        for(tempTarget in targetList){

            val response = try{
               //TODO build Request body
                api.createTarget(null)
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

            }else{
                Log.e("PVM", "Response not Successful")

            }
        }

         */
    }

    fun removeAllTargets(){
       targetList.clear()
        targets.value = targetList

    }
    fun getTargets(): LiveData<List<Target>>{
        return targets as LiveData<List<Target>>
    }




}