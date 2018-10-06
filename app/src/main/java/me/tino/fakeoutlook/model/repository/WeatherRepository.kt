package me.tino.fakeoutlook.model.repository

import io.reactivex.Maybe
import io.reactivex.Observable
import me.tino.fakeoutlook.API_KEY
import me.tino.fakeoutlook.App
import me.tino.fakeoutlook.model.WeatherInfo
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * singleton repository provide
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 October 06, 20:07.
 */
object WeatherRepository {

    private val weatherService by lazy(LazyThreadSafetyMode.NONE) {
        App.instance.retrofit.create(WeatherService::class.java)
    }

    fun getWeatherInfo(time: Long, geo: String = "30,120"): Observable<WeatherInfo> {
        return weatherService.getWeatherInfo(geo, time)
    }
}

/**
 * provide service for weather
 */
interface WeatherService {
    @GET("$API_KEY/{geo},{time}?units=auto&exclude=minutely,currently,alerts,offset,daily,flags")
    fun getWeatherInfo(@Path("geo") geo: String, @Path("time") time: Long): Observable<WeatherInfo>
}