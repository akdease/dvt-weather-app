package com.weather.app.viewModels

import androidx.lifecycle.ViewModel
import com.weather.app.utilities.WeatherConstants

open class BaseViewModel : ViewModel() {
    fun getWeatherDataParamList(lat: Double, lon: Double): HashMap<String, String> {
        val queryMap: HashMap<String, String> = HashMap()
        queryMap.put(WeatherConstants.PARAM_API_LAT, lat.toString())
        queryMap.put(WeatherConstants.PARAM_API_LON, lon.toString())
        queryMap.put(WeatherConstants.PARAM_APP_ID, WeatherConstants.WEATHER_API_KEY)
        return queryMap
    }
}