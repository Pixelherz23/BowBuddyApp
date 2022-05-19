package com.example.bowbuddyapp.api.requests

import com.example.bowbuddyapp.api.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun buildService(): ApiRequests {
        return retrofit.create(ApiRequests::class.java)
    }
}