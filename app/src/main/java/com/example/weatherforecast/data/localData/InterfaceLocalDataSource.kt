package com.example.weatherforecast.data.localData

import androidx.lifecycle.LiveData
import com.example.weatherforecast.model.Alarm
import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.model.WeatherRespond

interface InterfaceLocalDataSource {
    fun getAllWeather(): LiveData<List<WeatherRespond>>
    fun getWeather(timezone:String): WeatherRespond
    fun getListWeather():List<WeatherRespond>
    fun getWeatherByLatAndLon(lat: String, lng: String): LiveData<WeatherRespond>
    suspend fun insert(weather: WeatherRespond?)
    suspend fun update(weather: WeatherRespond?)
    suspend fun deleteWeather(lat: String, lng: String)
    suspend fun insertAlarm(alarm: Alarm):Long
    fun getAlarmId(id:Int):Alarm
    fun getAlarm(): LiveData<List<Alarm>>
    fun deleteAlarm(alarm: Alarm)
}