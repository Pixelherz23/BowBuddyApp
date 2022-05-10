package com.example.bowbuddyapp

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*



//it is possible to use @Body requestBody: RequestBody. See https://square.github.io/retrofit/#api-declaration
interface BowBuddyAPI {

    @POST("/api/v1/create")
    suspend fun createParcours(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("/api/v1/create")
    suspend fun createEmployee(@Body requestBody: RequestBody): Response<ResponseBody>

    @GET("/api/v1/employees")
    suspend fun getEmployees(): Response<ResponseBody>




}
