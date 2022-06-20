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

/**
 *Object for dependency injection
 *
 *@author Lukas Beckmann, Kai-U. Stieler (co-author)
 *
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * The root url to the rest api
     *
     */
    private const val BASE_URL = "https://bow-buddy.ddns.net/api/"

    /**
     * builds and provide a singleton retrofit instance.
     * @return the [Retrofit] instance
     */
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /**
     * calling .create(ApiRequests::class.java)) on the given retrofit instance
     *
     * @param retrofit Pass the instance provided by provideRetrofit()
     * @return generates an implementation of the ApiRequests interface
     */
    @Singleton
    @Provides
    fun buildService(retrofit: Retrofit): ApiRequests =
        retrofit.create(ApiRequests::class.java)



    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    // Build a GoogleSignInClient with the options specified by GoogleSignInOptions.
    /**
     * Configures a sign-in to request the user's ID, email address, and basi
     * profile. ID and basic profile are included in DEFAULT_SIGN_IN.
     *
     * @param context Pass context of app
     * @return GoogleSignInClient with the options specified by GoogleSignInOptions.
     */
    @Singleton
    @Provides
    fun provideGoogleSignInClient(@ApplicationContext context : Context): GoogleSignInClient =
        GoogleSignIn.getClient(context, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build())


    /**
     * Provides the last signedIn account
     * @param context Pass context of app
     * @return the last account that the user signed in with.
     */

    @Provides
    fun provideGoogleSignInAcc(@ApplicationContext context : Context) : GoogleSignInAccount =  GoogleSignIn.getLastSignedInAccount(context)!!
}