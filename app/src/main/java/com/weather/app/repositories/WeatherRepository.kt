package com.weather.app.repositories

import android.content.Context
import com.weather.app.api.RetrofitImpl
import com.weather.app.models.ForecastModel
import com.weather.app.models.WeatherModel
import com.weather.app.utilities.StorageUtil
import retrofit2.Response

class WeatherRepository : BaseRepository() {

    private val retroFitImplementation = RetrofitImpl()
    private val storageUtil = StorageUtil()

    suspend fun getWeatherData(
        context: Context,
        queryMap: HashMap<String, String>,
    ): Response<WeatherModel>? {
        if (shouldUseInternet(WeatherModel::class.java)) {
            return retroFitImplementation.getRetrofit().getWeatherData(queryMap)
        } else
            return storageUtil.getObjectByKey(
                context,
                WeatherModel::class.java.canonicalName
            ) as? Response<WeatherModel>
    }

    suspend fun getForecastData(
        context: Context,
        queryMap: HashMap<String, String>,
    ): Response<ForecastModel>? {
        if (shouldUseInternet(ForecastModel::class.java))
            return retroFitImplementation.getRetrofit().getForecastData(queryMap)
        else
            return storageUtil.getObjectByKey(
                context,
                ForecastModel::class.java.canonicalName
            ) as? Response<ForecastModel>
    }
}