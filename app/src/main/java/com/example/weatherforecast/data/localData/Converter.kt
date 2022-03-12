package com.example.weatherforecast.data.localData

import androidx.room.TypeConverter
import com.example.weatherforecast.model.Weather
import com.google.gson.Gson

class Converter {
    @TypeConverter
    fun json_to_list_weather(value:String)=
        Gson().fromJson(value,Array<Weather>::class.java).toList()
}