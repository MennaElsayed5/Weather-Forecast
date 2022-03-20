package com.example.weatherforecast.ui.alerts.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.weatherforecast.data.localData.LocalDataSource
import com.example.weatherforecast.model.Alarm
import com.example.weatherforecast.model.Repository
import kotlinx.coroutines.launch

class AlertsViewModel (application: Application) : AndroidViewModel(application){
    val repository: Repository = Repository.getRepoInstance(application)
    var id=MutableLiveData<Long>()
      fun insertAlarm(alarm: Alarm){
          viewModelScope.launch {
             id.postValue(repository.insertAlarm(alarm))
          }
    }
    fun getAlarm(): LiveData<List<Alarm>>{
        return repository.getAlarm()
    }
    fun deleteAlarm(alarm: Alarm){
        repository.deleteAlarm(alarm)
    }




}