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

    /**
     *  Makes a [GET] request for a user with the given [email]
     *  @return HTTP [Response] with the requested [User] in the body
     */
    @GET("user/{email}")
    suspend fun getUser(@Path("email") email: String): Response<User>

    /**
     *  Makes a [GET] request for the users connected to a game
     *  @param link the link of the [Game]
     *  @return HTTP [Response] with a List of the requested [User]s in the body
     */
    @GET("user/game")
    suspend fun getUserGame(@Query("link") link: String): Response<List<User>>

    /**
     *  Makes a [POST] request to create a new user in the database
     *  @param requestBody the [User] which is sent to the database
     *  @return HTTP [Response] to verify that the request was successful
     */
    @POST("user")
    suspend fun createUser(@Body requestBody: User): Response<ResponseBody>

    /**
     *  Makes a [PUT] request to assign a game to a user
     *  @param email the email from the [User] being updated
     *  @param link the link of the [Game] which will be assigned to the User
     *  @return HTTP [Response] to verify that the request was successful
     */
    @PUT("user/{email}")
    suspend fun updateUserGame(@Path("email") email: String, @Query("link") link: String): Response<ResponseBody>

    /**
     *  Makes a [GET] request for the parcours of a user
     *  @param email the email of the [User]
     *  @return HTTP [Response] with a List of the requested [Parcours]es in the body
     */
    @GET("parcours/{email}")
    suspend fun getParcours(@Path("email") email: String): Response<List<Parcours>>

    /**
     *  Makes a [POST] request to create a new parcours in the database
     *  @param requestBody the [Parcours] which is sent to the database
     *  @return HTTP [Response] to verify that the request was successful
     */
    @POST("parcours")
    suspend fun createParcours(@Body requestBody: Parcours): Response<ResponseBody>

    /**
     *  Makes a [GET] request for the stations of a parcours
     *  @param id the id of the [Parcours]
     *  @return HTTP [Response] with a List of the requested [Station]s in the body
     */
    @GET("station/{id}")
    suspend fun getStations(@Path("id") id: Int): Response<List<Station>>

    /**
     *  Makes a [POST] request to create a new station in the database
     *  @param requestBody a Json representation of Station (contains other attributes as the [Station] class) which is sent to the database
     *  @return HTTP [Response] to verify that the request was successful
     */
    @POST("station")
    suspend fun createStation(@Body requestBody: RequestBody): Response<ResponseBody>

    /**
     *  Makes a [GET] request for the targets of a station
     *  @param id the id of the [Station]
     *  @return HTTP [Response] with a List of the requested [Target]s in the body
     */
    @GET("target/{id}")
    suspend fun getTargets(@Path("id") id: Int): Response<List<Target>>

    /**
     *  Makes a [POST] request to create a new target in the database
     *  @param requestBody a Json representation of Target (contains other attributes as the [Target] class) which is sent to the database
     *  @return HTTP [Response] to verify that the request was successful
     */
    @POST("target")
    suspend fun createTarget(@Body requestBody: RequestBody): Response<ResponseBody>

    /**
     *  Makes a [GET] request for the statistics of a user
     *  @param email the email of the [User]
     *  @return HTTP [Response] with the requested [Statistics] in the body
     */
    @GET("statistics/{email}")
    suspend fun getStatistics(@Path("email") email: String): Response<Statistics>

    /**
     *  Makes a [GET] request for a specific game
     *  @param link the link of the specific [Game]
     *  @return HTTP [Response] with the requested [Game] in the body
     */
    @GET("game")
    suspend fun getGame(@Query("link") link: String): Response<Game>

    /**
     *  Makes a [POST] request to create a new game in the database
     *  @param requestBody  the [Game] which is sent to the database
     *  @return HTTP [Response] to verify that the request was successful
     */
    @POST("game/{email}")
    suspend fun createGame(@Path("email") email: String, @Body requestBody : Game): Response<ResponseBody>

    /**
     *  Makes a [GET] request for points on a target from a user in a game
     *  @param email the email from the [User]
     *  @param target the id of the [Target]
     *  @param link the link of the [Game]
     *  @return HTTP [Response] with the requested [Points] in the body
     */
    @GET("points/target/{email}/{target}")
    suspend fun getPointsTarget(@Path("email" ) email: String,
                                @Path("target" ) target: Int,
                                @Query("link" ) link: String ): Response<Points>

    /**
     *  Makes a [GET] request for points on a station from a user in a game
     *  @param email the email from the [User]
     *  @param station the id of the [Station]
     *  @param link the link of the [Game]
     *  @return HTTP [Response] with the requested [PointsStation] in the body
     */
    @GET("points/station/{email}/{station}")
    suspend fun getPointsStation(@Path("email" ) email: String,
                                 @Path("station" ) station: Int,
                                 @Query("link" ) link: String ): Response<PointsStation>

    /**
     *  Makes a [GET] request for points on a target from a user in a game
     *  @param email the email from the [User]
     *  @param parcours the id of the [Parcours]
     *  @param link the link of the [Game]
     *  @return HTTP [Response] with the requested [PointsParcours] in the body
     */
    @GET("points/parcours/{email}/{parcours}")
    suspend fun getPointsParcours(@Path("email" ) email: String,
                                  @Path("parcours", ) parcours: Int,
                                  @Query("link" ) link: String ): Response<PointsParcours>

    /**
     *  Makes a [PUT] request to update points
     *  @param requestBody the [Points] to update
     *  @return HTTP [Response] to verify that the request was successful
     */
    @PUT("points/target")
    suspend fun createPoints(@Body requestBody: Points): Response<ResponseBody>

    /**
     *  Makes a [DELETE] request to delete the specific parcours
     *  @param id the id of the [Parcours]
     *  @return HTTP [Response] to verify that the request was successful
     */
    @DELETE("parcours/{id}")
    suspend fun deleteParcours(@Path("id") id: String): Response<ResponseBody>

    /**
     *  Makes a [GET] request for the number of hits in a parcour from a plyer in a game
     *  @param email the email from the [User]
     *  @param parcours the id of the [Parcours]
     *  @param link the link of the [Game]
     *  @return HTTP [Response] with the requested number of hits in the body
     */
    @GET("points/hits/{email}/{parcours}")
    suspend fun getHits(@Path("email" ) email: String,
                        @Path("parcours", ) parcours: Int,
                        @Query("link" ) link: String ): Response<Int>

    /**
     *  Makes a [GET] request for the number of targets in a parcour
     *  @param parcours the id of the [Parcours]
     *  @return HTTP [Response] with the requested number of targets in the body
     */
    @GET("targets/count/{parcours}")
    suspend fun getMaxTargets(@Path("parcours", ) parcours: Int): Response<Int>

    /**
     *  Makes a [DELETE] request to delete the specific game
     *  @param link the link of the [Game]
     *  @return HTTP [Response] to verify that the request was successful
     */
    @DELETE("game")
    suspend fun deleteGame(@Query("link") link: String): Response<ResponseBody>

    /**
     *  Makes a [PUT] request to update statistics of a user
     *  @param email the email from the [User]
     *  @param requestBody the [Statistics] to update
     *  @return HTTP [Response] to verify that the request was successful
     */
    @PUT("statistics/{email}")
    suspend fun updateStatistics(@Path("email") email: String, @Body requestBody: Statistics): Response<ResponseBody>
}