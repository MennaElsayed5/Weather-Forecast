package com.example.weatherforecast.ui.Fav.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.WeatherRespond

class MapViewModel (application: Application): AndroidViewModel(application) {
    var liveDataWeather = MutableLiveData<WeatherRespond>()
    val repository:Repository= Repository.getRepoInstance(application)
    fun setDataLocation (lat:String,lon:String){
        repository.setData(lat,lon)
    }


}