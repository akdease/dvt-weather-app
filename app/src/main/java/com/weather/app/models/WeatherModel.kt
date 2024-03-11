package com.weather.app.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeatherModel(
    val coord: Coord,
    val weather: ArrayList<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val rain: Rain,
    val clouds: Clouds,
    val dt: Int = 0,
    val sys: Sys,
    val timezone: String,
    val id: Int,
    val name: String,
    val cod: Int,
) : Serializable


data class Coord(
    val lat: Double,
    val lon: Double
) : Serializable

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Long,
    val humidity: Long,
    val sea_level: Long,
    val grnd_level: Long,
    val temp_kf: Double?
) : Serializable

data class Wind(
    val speed: Double,
    val deg: Long,
    val gust: Double
) : Serializable

data class Rain(
    @SerializedName("1h")
    val oneHour: Double
) : Serializable

data class Sys(
    val type: Int?,
    val id: Int?,
    val country: String?,
    val sunrise: Int?,
    val sunset: Int?,
    val pod: String?
) : Serializable

class Clouds(
    val all: Long
) : Serializable

data class Weather(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
) : Serializable
