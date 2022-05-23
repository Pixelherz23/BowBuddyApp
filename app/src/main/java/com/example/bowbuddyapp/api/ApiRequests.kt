package com.example.bowbuddyapp.api.requests
import com.example.bowbuddyapp.data.*
import com.example.bowbuddyapp.data.Target
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiRequests {

    @GET("user/{email}")
    suspend fun getUser(@Path("email") email: String): Response<User>

    @POST("user")
    suspend fun createUser(@Body requestBody: RequestBody): Response<ResponseBody>

    @GET("parcours/{email}")
    suspend fun getParcours(@Path("email") email: String): Response<List<Parcours>>

    @POST("parcours")
    suspend fun createParcours(@Body requetBody: RequestBody): Response<ResponseBody>

    @GET("station/{id}")
    suspend fun getStations(@Path("id") id: Int): Response<List<Station>>

    @POST("station")
    suspend fun createStation(@Body requetBody: RequestBody): Response<ResponseBody>

    @GET("target/{id}")
    suspend fun getTarget(@Path("id") id: Int): Response<List<Target>>

    @POST("target")
    suspend fun createTarget(@Body requetBody: RequestBody): Response<ResponseBody>

    @GET("statistics/{email}")
    suspend fun getStatistics(@Path("email") email: String): Response<Statistics>

    @GET("game/{link}")
    suspend fun getGame(@Path("link") link: String): Response<Game>

    @POST("game")
    suspend fun createGame(@Body requetBody : Game): Response<ResponseBody>

    @GET("points/target/{email}/{link}/{target}")
    suspend fun getPointsTarget(@Path("email" ) email: String,
                                @Path("link" ) link: String,
                                @Path("target" ) target: String): Response<Int>

    @GET("points/station/{email}/{link}/{station}")
    suspend fun getPointsStation(@Path("email" ) email: String,
                                @Path("link" ) link: String,
                                @Path("station" ) station: String): Response<Int>

    @GET("points/parcours/{email}/{link}/{parcours}")
    suspend fun getPointsParcours(@Path("email" ) email: String,
                                 @Path("link" ) link: String,
                                 @Path("parcours" ) parcours: String): Response<Int>

    @POST("points/target")
    suspend fun createPoints(@Body requetBody: RequestBody): Response<ResponseBody>


}