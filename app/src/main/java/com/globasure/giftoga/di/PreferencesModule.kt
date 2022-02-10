package com.globasure.giftoga.di

import android.app.Application
import com.globasure.giftoga.utils.HawkHelper
import com.orhanobut.hawk.Hawk
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun provideUserPreferences(application: Application): HawkHelper {
        Hawk.init(application).build()
        return HawkHelper()
    }
}