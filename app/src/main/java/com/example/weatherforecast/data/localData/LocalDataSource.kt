package com.example.weatherforecast.data.localData

import androidx.lifecycle.LiveData
import com.example.weatherforecast.model.Alarm
import com.example.weatherforecast.model.WeatherRespond

class LocalDataSource (private val dao: Dao) : InterfaceLocalDataSource {

    override fun getAllWeather(): LiveData<List<WeatherRespond>> {
        return dao.getAllWeather()
    }

    override fun getWeather(timezone:String): WeatherRespond {
        return dao.getWeatherApi(timezone)
    }

    override fun getListWeather(): List<WeatherRespond> {
        return dao.getListWeather()
    }

    override fun getWeatherByLatAndLon(lat: String, lng: String): LiveData<WeatherRespond> {
        return dao.getWeatherByLatLon(lat, lng)
    }


    override suspend fun insert(weather: WeatherRespond?) {
        weather?.let { dao.insert(it) }
    }

    override suspend fun update(weather: WeatherRespond?){
        weather?.let { dao.update(it) }
    }

    override suspend fun deleteWeather(lat: String, lng: String) {
        dao.deleteWeather(lat, lng)
    }

    override suspend  fun insertAlarm(alarm: Alarm):Long {
       return dao.insertAlarm(alarm)
    }

    override fun getAlarmId(id: Int): Alarm {
        return dao.getAlarmId(id)
    }

    override fun getAlarm(): LiveData<List<Alarm>> {
        return dao.getAlarm()
    }

    override fun deleteAlarm(alarm: Alarm) {
        dao.deleteAlarm(alarm)
    }


}