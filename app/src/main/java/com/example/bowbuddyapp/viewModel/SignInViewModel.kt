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

/**
 * ViewModel for the Sign In Activity
 * @author Kai-U. Stieler
 */
@HiltViewModel
class SignInViewModel @Inject constructor(private var api: ApiRequests, application: Application): AndroidViewModel(application) {

    val isInDatabase = MutableLiveData<Boolean>()
    val user = MutableLiveData<User>()

    /**
     * checks if the user exists in DB. Depending on the result, [isInDatabase] will be true or false
     */
    fun getUser() {
        viewModelScope.launch() {

            val response = try {
                api.getUser(user.value!!.email)

            } catch (e: IOException) {
                Log.e("SIVM", "IOException, you might not have internet connection")

                return@launch
            } catch (e: HttpException) {
                Log.e("SIVM", "HttpException, unexpected response")

                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                Log.i("SIVM", "Response Successful, User in Database found")
                isInDatabase.value = true

            } else if (response.code() == 404) {
                Log.i("SIVM", "User not in Database")
                isInDatabase.value = false

            } else {
                Log.e("SIVM", "Response not Successful")

            }

        }
    }

    /**
     * sending user to database. Depeding if [isInDatabase] is true
     */
    fun createUser() {
        if (isInDatabase.value == false) {
            viewModelScope.launch() {
                val response = try {
                    api.createUser(user.value!!)

                } catch (e: IOException) {
                    Log.e("SIVM", "IOException, you might not have internet connection")

                    return@launch
                } catch (e: HttpException) {
                    Log.e("SIVM", "HttpException, unexpected response")

                    return@launch
                }
                if (response.isSuccessful && response.body() != null) {
                    Log.i("SIVM", "Response Successful, User in Database created" )
                    isInDatabase.value = true

                } else {
                    Log.e("SIVM", "Response not Successful")
                }

            }

        }
    }

}