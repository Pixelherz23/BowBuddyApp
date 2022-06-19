package com.example.bowbuddyapp.di

import android.content.Context
import com.example.bowbuddyapp.api.requests.ApiRequests
import com.example.bowbuddyapp.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://bow-buddy.ddns.net/api/"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Singleton
    @Provides
    fun buildService(retrofit: Retrofit): ApiRequests =
        retrofit.create(ApiRequests::class.java)



    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    // Build a GoogleSignInClient with the options specified by GoogleSignInOptions.
    @Singleton
    @Provides
    fun provideGoogleSignInClient(@ApplicationContext context : Context): GoogleSignInClient =
        GoogleSignIn.getClient(context, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build())

    @Provides
    fun provideGoogleSignInAcc(@ApplicationContext context : Context) : GoogleSignInAccount =  GoogleSignIn.getLastSignedInAccount(context)!!
}