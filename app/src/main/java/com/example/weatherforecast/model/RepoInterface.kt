package com.example.weatherforecast.model

import android.content.Context
import androidx.lifecycle.LiveData
import retrofit2.Response

interface RepoInterface {
//    fun getWeatherApi(lat:String,log:String):Weather
//    fun weatherData(context: Context,lat: String,log: String):LiveData<List<Weather>>
    fun getAllWeather(): LiveData<List<WeatherRespond>>
    fun getWeather(timezone:String): WeatherRespond
    fun getWeatherByLatAndLon(lat: String, lng: String): LiveData<WeatherRespond>
    suspend fun insert(weather: WeatherRespond?)
    suspend fun update(weather: WeatherRespond?)
    suspend fun getCurrentWeatherData(lat: String, lon: String, exclude: String, units: String, lang: String, ): WeatherRespond
}