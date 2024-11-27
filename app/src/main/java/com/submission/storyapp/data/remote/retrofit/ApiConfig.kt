package com.submission.storyapp.data.remote.retrofit

import com.submission.storyapp.BuildConfig
import com.submission.storyapp.BuildConfig.BaseURL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun getApiService(): ApiService {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

//        val authInterceptor = Interceptor { chain ->
//            val req = chain.request()
//            val requestHeaders = req.newBuilder()
//                .addHeader("Authorization", "Bearer $token")
//                .build()
//            chain.proceed(requestHeaders)
//        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
//            .addInterceptor(authInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}