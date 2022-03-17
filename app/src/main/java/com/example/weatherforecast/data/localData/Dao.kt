package com.example.weatherforecast.data.localData

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.model.WeatherRespond
@Dao
interface Dao {
    @Query("SELECT * FROM weather")
    fun getAllWeather(): LiveData<List<WeatherRespond>>

    @Query("SELECT * FROM weather")
    fun getListWeather():List<WeatherRespond>

    @Query("SELECT * FROM weather WHERE timezone = :timezone ")
    fun getWeatherApi(timezone:String): WeatherRespond

    @Query("SELECT * FROM weather WHERE lat=:lat AND lon=:lng ")
    fun getWeatherByLatLon(lat: String, lng: String): LiveData<WeatherRespond>

    @Query("SELECT * FROM weather WHERE lat=:lat AND lon=:lng ")
    fun getWeatherLocation(lat: String, lng: String): WeatherRespond

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: WeatherRespond)

    @Update
   suspend fun update(weather: WeatherRespond)

    @Query("DELETE FROM Weather")
    suspend fun deleteAll()

    @Query("DELETE FROM weather WHERE  lat=:lat AND lon=:lon")
    suspend fun deleteWeather(lat: String, lon: String)

//    @Delete
//    suspend fun delete(weather: Weather): Void

}