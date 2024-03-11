package com.weather.app.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity

class LocationUtil {

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(context: Context, locationListener: LocationListener): Location? {
        val locationManager =
            context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

        val providers: List<String> = locationManager.getProviders(true)
        var bestLocation: Location? = null

        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                15f,
                locationListener
            )
        } catch (ex: SecurityException) {
            ex.printStackTrace()
        }

        for (provider in providers) {
            val location = locationManager.getLastKnownLocation(provider)
            if (location != null &&
                (bestLocation == null || location.accuracy < (bestLocation.accuracy))
            ) {
                bestLocation = location
            }
        }

        return bestLocation
    }
}