package com.globasure.giftoga

import android.app.Application
import co.paystack.android.PaystackSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp

class GiftOgaApplication : Application(), Thread.UncaughtExceptionHandler {

    override fun onCreate() {
        super.onCreate()
        setDebugLogging()
        PaystackSdk.initialize(applicationContext)
    }

    private fun setDebugLogging() {
        @Suppress("ConstantConditionIf")
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        Timber.w(throwable, "Uncaught Exception!")
    }
}