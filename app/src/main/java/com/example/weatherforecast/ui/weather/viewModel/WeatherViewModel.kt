package com.example.weatherforecast.ui.weather.viewModel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.model.WeatherRespond
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WeatherViewModel(private val repository: Repository) : ViewModel() {
    var liveDataWeather = MutableLiveData<WeatherRespond>()
    lateinit var respond: WeatherRespond
    fun insertWeatherData(
        lat: String,
        lon: String,
        exclude: String,
        units: String,
        lang: String
    ) {

        val job = viewModelScope.launch(Dispatchers.IO) {
//            lat = sharedPreferences.getString("lat","0").toString()
            respond = repository.getCurrentWeatherData(lat, lon, exclude, units, lang)
            repository.insert(respond)

        }
        viewModelScope.launch(Dispatchers.IO) {
            job.join()
            val responseWeather = repository.getWeather(respond.timezone)
            liveDataWeather.postValue(responseWeather)
        }

    }
}