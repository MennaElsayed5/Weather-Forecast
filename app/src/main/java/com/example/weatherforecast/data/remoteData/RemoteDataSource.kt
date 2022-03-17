package com.example.weatherforecast.data.remoteData

import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.model.WeatherRespond
import retrofit2.Response
import retrofit2.http.Query

interface RemoteDataSource {
    suspend fun getCurrentWeatherData(
        lat: String,
        lon: String,
        exclude: String,
        units: String,
        lang: String,
    ): Response<WeatherRespond>
}