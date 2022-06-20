package com.example.bowbuddyapp.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Is necessary for Dagger Hilt
 * @see [HiltAndroidApp]
 */
@HiltAndroidApp
class CurrencyApplication: Application()