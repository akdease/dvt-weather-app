package com.weather.app.api

import com.weather.app.models.ForecastModel
import com.weather.app.models.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {
    @GET("/data/2.5/weather")
    suspend fun getWeatherData(
        @QueryMap queryMap: Map<String, String>
    ): Response<WeatherModel>

    @GET("/data/2.5/forecast")
    suspend fun getForecastData(
        @QueryMap queryMap: Map<String, String>
    ): Response<ForecastModel>
}