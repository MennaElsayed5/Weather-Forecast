package com.example.weatherforecast.ui.Fav.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.WeatherRespond

class FavViewModel (application: Application): AndroidViewModel(application) {
//    var liveDataWeather = MutableLiveData<WeatherRespond>()
    val repository: Repository = Repository.getRepoInstance(application)
    fun getFavWeather():LiveData<List<WeatherRespond>>{
        return repository.getAllWeather()
    }

    fun deleteFromFav(lat:String,lon:String){
        repository.deleteWeather(lat,lon)
    }

}