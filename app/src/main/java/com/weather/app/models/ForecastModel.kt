package com.weather.app.models

import java.io.Serializable

data class ForecastModel(
    val cod: String,
    val message: Long,
    val cnt: Long,
    val list: List<ForecastData>,
    val city: City,
) : Serializable

data class ForecastData(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Long,
    val pop: Double,
    val rain: Rain,
    val sys: Sys,
    val dt_txt: String,
)

data class City(
    val id: Long,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Long,
    val timezone: Long,
    val sunrise: Long,
    val sunset: Long,
)