package com.weather.app.api

import com.weather.app.utilities.WeatherConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitImpl {

    private val okHttpClient = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(getBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private fun getBaseUrl(): String {
        return WeatherConstants.WEATHER_API_HOST
    }

    fun getRetrofit(): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}