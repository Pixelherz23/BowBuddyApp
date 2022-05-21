package com.example.bowbuddyapp.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bowbuddyapp.api.requests.ApiRequests
import com.example.bowbuddyapp.data.Parcours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ParcoursViewModel @Inject constructor(private var api: ApiRequests): ViewModel() {
    private val parcoursLiveData = MutableLiveData<List<Parcours>>()
    val parcours: LiveData<List<Parcours>> = parcoursLiveData

    private val pbVisibilityLiveData = MutableLiveData<Int>()
    val pbVisibility: LiveData<Int> = pbVisibilityLiveData

    init {
        fetchData("test@api.com")
    }

    fun fetchData(email: String){
        viewModelScope.launch() {
            pbVisibilityLiveData.value = View.VISIBLE
            val response = try{
                api.getParcours(email)
            } catch(e: IOException){
                Log.e("PVM", "IOException, you might not have internet connection")
                pbVisibilityLiveData.value = View.GONE
                return@launch
            } catch (e: HttpException){
                Log.e("PVM", "HttpException, unexpected response")
                pbVisibilityLiveData.value = View.GONE
                return@launch
            }
            if(response.isSuccessful && response.body() != null) {

                parcoursLiveData.value = response.body()!!

            }else{
                Log.e("PVM", "Response not Successful")
            }
            pbVisibilityLiveData.value = View.GONE
        }
    }
}