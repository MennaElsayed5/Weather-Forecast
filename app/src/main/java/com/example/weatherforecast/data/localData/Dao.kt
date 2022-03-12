package com.example.weatherforecast.data.localData

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.model.WeatherRespond

interface Dao {
    @Query("SELECT * FROM weather")
    fun getAllWeather(): LiveData<List<Weather>>

//    @Query("SELECT * FROM weather")
//    fun getListWeather():List<Weather>

    @Query("SELECT * FROM weather WHERE lat=lat AND lon=lon")
    fun getWeatherApi(lat: String, lan: String): Weather

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weather: Weather)

    @Query("DELETE FROM Weather")
    suspend fun deleteAll()

    @Query("DELETE FROM weather WHERE  lat=lat AND lon=lon")
    suspend fun deleteWeather(lat: String, lan: String)

//    @Delete
//    suspend fun delete(weather: Weather): Void

}