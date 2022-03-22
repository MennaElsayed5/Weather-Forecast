package com.example.weatherforecast.model

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.data.localData.DataBase
import com.example.weatherforecast.data.localData.InterfaceLocalDataSource
import com.example.weatherforecast.data.localData.LocalDataSource
import com.example.weatherforecast.data.remoteData.NetworkApi
import com.example.weatherforecast.data.remoteData.RemoteDataSource
import kotlinx.coroutines.*

class Repository private constructor(
    private var localData: InterfaceLocalDataSource,
    private var remoteData: RemoteDataSource
) : RepoInterface {
    companion object {
        @Volatile
        private var INSTANCE: Repository? = null
        fun getRepoInstance(application: Application): Repository {
            return INSTANCE ?: synchronized(this) {
                Repository(
                    LocalDataSource(DataBase.getDatabase(application).dao()),
                    NetworkApi
                ).also {
                    INSTANCE = it
                }
            }
        }

        fun getRepoInstance(context: Context): Repository {
            return INSTANCE ?: synchronized(this) {
                Repository(
                    LocalDataSource(DataBase.getDatabase(context).dao()),
                    NetworkApi
                ).also {
                    INSTANCE = it
                }
            }
        }
    }

    lateinit var liveDataWeather: LiveData<List<WeatherRespond>>

    override fun getAllWeather(): LiveData<List<WeatherRespond>> {
        return localData.getAllWeather()
    }

    override fun getWeather(timezone: String): WeatherRespond {
        return localData.getWeather(timezone)
    }

    override fun getWeatherByLatAndLon(lat: String, lng: String): LiveData<WeatherRespond> {
//         val exceptionHandlerException = CoroutineExceptionHandler { _, throwable ->
//             throwable.printStackTrace()
//             Log.i("id", "exception")
//         }
//         CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
//             val response = NetworkApi.getCurrentWeatherData(lat, lng, "minutely", "units", "lang")
//             withContext(Dispatchers.Main) {
//                 if (response.isSuccessful) {
//                 liveDataWeather = localData.getWeatherByLatAndLon(lat,lng)
//                     //  weatherLiveData.postValue(response.body())
//                 }
//             }
//         }
//         return liveDataWeather
        return localData.getWeatherByLatAndLon(lat, lng)
    }

    fun setData(lat: String, lng: String) {

        val exceptionHandlerException = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Log.i("id", "exception")
        }
        CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
            val response = NetworkApi.getCurrentWeatherData(lat, lng, "minutely", "units", "lang")
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    localData.insert(response.body())
                }
            }
        }
    }

    override suspend fun insert(weather: WeatherRespond?) {
        val exceptionHandlerException = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Log.i("id", "exception")
        }
        CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
            localData.insert(weather)

        }
    }

    override suspend fun update(weather: WeatherRespond?) {
        localData.update(weather)
    }

    fun deleteWeather(lat: String, lng: String) {
        CoroutineScope(Dispatchers.IO).launch {
            localData.deleteWeather(lat, lng)
        }
    }

    override suspend fun getCurrentWeatherData(
        lat: String,
        lon: String,
        exclude: String,
        units: String,
        lang: String
    ): WeatherRespond {
        val response = remoteData.getCurrentWeatherData(lat, lon, "minutely", units, lang)
        if (response.isSuccessful) {
            return response.body()!!
        } else
            throw Exception("${response.errorBody()}")
    }

    override fun getAlarmId(id: Int): Alarm {
        return localData.getAlarmId(id)
    }

    var ins: Long = 0
    suspend fun insertAlarm(alarm: Alarm): Long {
        val job = CoroutineScope(Dispatchers.IO).launch {
            ins = localData.insertAlarm(alarm)
        }
        job.join()
        return ins
    }

    fun getAlarm(): LiveData<List<Alarm>> {

        return localData.getAlarm()
    }

    fun deleteAlarm(alarm: Alarm) {
        CoroutineScope(Dispatchers.IO).launch {
            localData.deleteAlarm(alarm)
        }
    }

    fun refresh() {
        val exceptionHandlerException = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }
        CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
            var list = localData.getListWeather()
            for (item in list!!) {
                setData(item.lat.toString(), item.lon.toString())
            }
        }

    }

}