package com.example.weatherforecast.ui.settings.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.weatherforecast.model.Repository

class SettingsViewModel (application: Application) : AndroidViewModel(application) {
    val repository:Repository= Repository.getRepoInstance(application)

    fun refreshData(){
        repository.refresh()
    }
}