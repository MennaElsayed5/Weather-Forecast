package com.example.weatherforecast.data.remoteData

import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.model.WeatherRespond
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

val app_id = "14aa54575943ef5059af738a305949d9"

interface WeatherApi {
    @GET("data/2.5/onecall")
    suspend fun getCurrentWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String,
        @Query("units") units: String,
        @Query("lang") lang: String,
        @Query("appid") appid: String = app_id
    ): Response<WeatherRespond>


}