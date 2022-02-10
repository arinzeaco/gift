package com.globasure.giftoga.di

import com.globasure.giftoga.BuildConfig
import com.globasure.giftoga.utils.HawkHelper
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor, hawkHelper: HawkHelper): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .addNetworkInterceptor(loggingInterceptor)
            .addInterceptor(getHeaderInterceptor(hawkHelper))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_PROD)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private fun getHeaderInterceptor(hawkHelper: HawkHelper): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()

            request = request.newBuilder()
                .addHeader("Authorization", hawkHelper.getToken())
                .addHeader("Content-type", "application/json")
                .build()

            Timber.d(
                String.format(
                    "--> Sending request %s on %s%n%s",
                    request.url,
                    chain.connection(),
                    request.headers
                )
            )

            chain.proceed(request)
        }
    }
}