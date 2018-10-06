package me.tino.fakeoutlook.model

import com.google.gson.annotations.SerializedName

/**
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 October 06, 19:50.
 */
data class WeatherSectionInfo(
    val icon: String,
    val temperature: Int
)

data class WeatherInfo(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val hourly: Hourly,
    val offset: Int
)

data class Hourly(
    val summary: String,
    val icon: String,
    val data: List<Data>
)

data class Data(
    val time: Long,
    val summary: String,
    val icon: String,
    val precipIntensity: Double,
    val precipProbability: Double,
    val temperature: Double,
    val apparentTemperature: Double,
    val dewPoint: Double,
    val humidity: Double,
    val pressure: Double,
    val windSpeed: Double,
    val windGust: Double,
    val windBearing: Int,
    val cloudCover: Double,
    val uvIndex: Int,
    val visibility: Double,
    val ozone: Double,
    val precipType: String
)
