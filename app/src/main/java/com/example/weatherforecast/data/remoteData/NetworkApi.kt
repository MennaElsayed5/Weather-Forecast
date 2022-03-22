package com.example.weatherforecast.data.remoteData

import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.model.WeatherRespond
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkApi : RemoteDataSource {
    private const val BASE_URL = "https://api.openweathermap.org/"
    private fun getCurrentData(): WeatherApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(WeatherApi::class.java)

    }

    override suspend fun getCurrentWeatherData(
        lat: String,
        lon: String,
        exclude: String,
        units: String,
        lang: String
    ): Response<WeatherRespond> =
        getCurrentData().getCurrentWeatherData(lat, lon, exclude, units, lang)


}