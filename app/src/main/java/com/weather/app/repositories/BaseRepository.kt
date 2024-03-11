package com.weather.app.repositories

open class BaseRepository {

    var hasInternet = true

    fun <T> shouldUseInternet(api: T): Boolean {
        return hasInternet
    }

}