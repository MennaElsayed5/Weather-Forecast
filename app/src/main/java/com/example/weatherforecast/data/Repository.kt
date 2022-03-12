package com.example.weatherforecast.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.data.localData.LocalDataSource
import com.example.weatherforecast.data.remoteData.NetworkApi
import com.example.weatherforecast.data.remoteData.WeatherApi
import com.example.weatherforecast.model.Weather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*

class Repository {
    lateinit var localData: LocalDataSource
    lateinit var remoteData: NetworkApi
    lateinit var liveDataWeather: MutableLiveData<Weather>

    constructor(application: Application) {
        localData = LocalDataSource(application)
        remoteData = NetworkApi
    }

    fun getWeatherLocalSource(lat: String, lng: String): Weather {
        return localData.getWeather(lat, lng)
    }

    fun getData(
        context: Context,
        lat: String,
        lng: String,
    ): LiveData<List<Weather>> {
        CoroutineScope(Dispatchers.IO).launch {
            val response = remoteData.getCurrentData().getCurrentWeatherData(lat, lng,"minutely","metric","en")
            try {
                if (response.isSuccessful) {
                    response.body()?.let {
                        localData.insert(it)
                        liveDataWeather.postValue(it)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return localData.getAllWeather()
    }


}