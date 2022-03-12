package com.example.weatherforecast.data.localData

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.weatherforecast.model.Weather

class LocalDataSource {
    var dao: Dao

    constructor(application: Application) {
        dao = DataBase.getDatabase(application).dao()
    }

    fun getAllWeather(): LiveData<List<Weather>> {
        return dao.getAllWeather()
    }

    fun getWeather(lat: String, lng: String): Weather {
        return dao.getWeatherApi(lat, lng)
    }

    suspend fun insert(weather: Weather?) {
        weather?.let { dao.insert(it) }
    }

    suspend fun deleteWeather(lat: String, lng: String) {
        dao.deleteWeather(lat, lng)
    }
}