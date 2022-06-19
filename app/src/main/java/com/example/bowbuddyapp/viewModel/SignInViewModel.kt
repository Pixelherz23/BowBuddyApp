package com.example.bowbuddyapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bowbuddyapp.api.requests.ApiRequests
import com.example.bowbuddyapp.data.Parcours
import com.example.bowbuddyapp.data.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.OkHttp
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private var api: ApiRequests, application: Application): AndroidViewModel(application) {

    val isInDatabase = MutableLiveData<Boolean>()
    val user = MutableLiveData<User>()


    fun getUser() {
        viewModelScope.launch() {

            val response = try {
                api.getUser(user.value!!.email)

            } catch (e: IOException) {
                Log.e("getUser", "IOException, you might not have internet connection")

                return@launch
            } catch (e: HttpException) {
                Log.e("getUser", "HttpException, unexpected response")

                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                Log.i("getUser", "Response Successful, User in Database found")

                isInDatabase.value = true

            } else if (response.code() == 404) {
                Log.i("getUser", "User not in Database")
                isInDatabase.value = false

            } else {
                Log.e("getUser", "Response not Successful: ${response.toString()}")

            }

        }
    }


    fun createUser() {
        if (isInDatabase.value == false) {
            viewModelScope.launch() {
                val response = try {
                    api.createUser(user.value!!)

                } catch (e: IOException) {
                    Log.e("createUser(", "IOException, you might not have internet connection")

                    return@launch
                } catch (e: HttpException) {
                    Log.e("createUser(", "HttpException, unexpected response")

                    return@launch
                }
                if (response.isSuccessful && response.body() != null) {
                    Log.i("SIVM", "Response Successful, User in Database created" )
                    isInDatabase.value = true

                } else {
                    Log.e("createUser", "Response not Successful: ${response.toString()}")
                }

            }

        }
    }

}