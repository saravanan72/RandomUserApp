package com.ztask.me.randomuser

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class RandomUserApplication() : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            Timber.plant(Timber.DebugTree())
        }
    }
}