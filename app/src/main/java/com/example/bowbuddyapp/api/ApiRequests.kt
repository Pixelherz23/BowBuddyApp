package com.example.bowbuddyapp.api.requests
import com.example.bowbuddyapp.data.*
import com.example.bowbuddyapp.data.Target
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 *Defining the available requests that the Server provides
 *
 *@author Lukas Beckmann, Kai-U. Stieler (co-author)
 *
 *
 */
interface ApiRequests {

    @GET("user/{email}")
    suspend fun getUser(@Path("email") email: String): Response<User>

    @POST("user")
    suspend fun createUser(@Body requestBody: User): Response<ResponseBody>

    @GET("parcours/{email}")
    suspend fun getParcours(@Path("email") email: String): Response<List<Parcours>>

    @POST("parcours")
    suspend fun createParcours(@Body requestBody: Parcours): Response<ResponseBody>

    @GET("station/{id}")
    suspend fun getStations(@Path("id") id: Int): Response<List<Station>>

    @POST("station")
    suspend fun createStation(@Body requestBody: RequestBody): Response<ResponseBody>

    @GET("target/{id}")
    suspend fun getTargets(@Path("id") id: Int): Response<List<Target>>

    @POST("target")
    suspend fun createTarget(@Body requestBody: RequestBody): Response<ResponseBody>

    @GET("statistics/{email}")
    suspend fun getStatistics(@Path("email") email: String): Response<Statistics>

    @GET("game")
    suspend fun getGame(@Query("link") link: String): Response<Game>

    @POST("game/{email}")
    suspend fun createGame(@Path("email") email: String, @Body requestBody : Game): Response<ResponseBody>

    @GET("points/target/{email}/{target}")
    suspend fun getPointsTarget(@Path("email" ) email: String,
                                @Path("target" ) target: Int,
                                @Query("link" ) link: String ): Response<Points>

    @GET("points/station/{email}/{station}")
    suspend fun getPointsStation(@Path("email" ) email: String,
                                 @Path("station" ) station: Int,
                                 @Query("link" ) link: String ): Response<PointsStation>

    @GET("points/parcours/{email}/{parcours}")
    suspend fun getPointsParcours(@Path("email" ) email: String,
                                  @Path("parcours", ) parcours: Int,
                                  @Query("link" ) link: String ): Response<PointsParcours>

    @PUT("points/target")
    suspend fun createPoints(@Body requestBody: Points): Response<ResponseBody>

    @DELETE("parcours/{id}")
    suspend fun deleteParcours(@Path("id") id: String): Response<ResponseBody>

}