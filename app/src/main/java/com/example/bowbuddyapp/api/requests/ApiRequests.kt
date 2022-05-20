package com.example.bowbuddyapp.api.requests
import com.example.bowbuddyapp.api.data.*
import com.example.bowbuddyapp.api.data.Target
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiRequests {

    @GET("/user/{email}")
    fun getUser(@Path("email") email: String): Call<User>

    @POST("/user")
    fun createUser(@Body requestBody: RequestBody): Response<ResponseBody>

    @GET("/parcours/{email}")
    fun getParcours(@Path("email") email: String): Call<Parcours>

    @POST("/parcours")
    fun createParcours(@Body requetBody: RequestBody): Response<ResponseBody>

    @GET("/station/{id}")
    fun getStations(@Path("id") id: Int): Call<Station>

    @POST("/station")
    fun createStation(@Body requetBody: RequestBody): Response<ResponseBody>

    @GET("/target/{id}")
    fun getTarget(@Path("id") id: Int): Call<Target>

    @POST("/target")
    fun createTarget(@Body requetBody: RequestBody): Response<ResponseBody>

    @GET("/statistics/{email}")
    fun getStatistics(@Path("email") email: String): Call<Statistics>
}