package com.example.weatherforecast.ui.weather.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.model.Repository

class FavViewModelFactory  (private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavDetailsViewModel(repository) as T
    }
}
