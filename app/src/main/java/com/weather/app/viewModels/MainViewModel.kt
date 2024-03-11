package com.weather.app.viewModels

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.weather.app.R
import com.weather.app.models.ForecastModel
import com.weather.app.models.WeatherModel
import com.weather.app.repositories.WeatherRepository
import com.weather.app.utilities.LocationUtil
import kotlinx.coroutines.*
import java.util.*

class MainViewModel(private val context: Context) : BaseViewModel() {

    var city: String? = null
    var lat: Double = 0.0
    var lon: Double = 0.0

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            lat = location.latitude
            lon = location.longitude
            setCityName()
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private val weatherRepository = WeatherRepository()
    private val locationUtil = LocationUtil()

    val errorMessage = MutableLiveData<String>()
    val mutableWeatherModel = MutableLiveData<WeatherModel>()
    val mutableForecastModel = MutableLiveData<ForecastModel>()
    val loading = MutableLiveData<Boolean>()

    var job: Job? = null

    fun getWeatherData() {
        loading.value = true
        job = CoroutineScope(Dispatchers.Default).launch {
            val response = weatherRepository.getWeatherData(
                context,
                getWeatherDataParamList(lat, lon)
            )
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    val body = response.body() as WeatherModel
                    mutableWeatherModel.postValue(body)
                    loading.value = false

                    Log.i(this.javaClass.canonicalName, "WEATHER JSON\n" + Gson().toJson(response.body()))
                } else {
                    onError("Api Error : ${response?.body()} ")
                }
            }
        }
    }

    fun getForecastData() {
        loading.value = true
        job = CoroutineScope(Dispatchers.Default).launch {
            val response = weatherRepository.getForecastData(
                context,
                getWeatherDataParamList(lat, lon)
            )
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    val body = response.body() as ForecastModel
                    mutableForecastModel.postValue(body)
                    loading.value = false
                    Log.i(this.javaClass.canonicalName, "FORECAST JSON\n" + Gson().toJson(response.body()))
                } else {
                    onError("Api Error : ${response?.body()} ")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun setCityName() {
        val geocoder = Geocoder(context, Locale.getDefault())
        var address: Address? = null

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> geocoder.getFromLocation(
                lat,
                lon,
                1
            ) {
                address = it.firstOrNull()
            }

            else -> address = geocoder.getFromLocation(
                lat,
                lon,
                1
            )?.firstOrNull()
        }

        city = address?.getLocality()
    }

    fun setLocation() {
        val location = locationUtil.getLastKnownLocation(context, locationListener)
        if (location == null) {
            Toast.makeText(
                context,
                context.getString(R.string.failed_getting_location),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        lat = location.latitude
        lon = location.longitude

        setCityName()
    }
}