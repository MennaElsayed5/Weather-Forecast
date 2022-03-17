package com.example.weatherforecast.ui.weather.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.WeatherRespond
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavDetailsViewModel (private val repository: Repository) : ViewModel() {
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