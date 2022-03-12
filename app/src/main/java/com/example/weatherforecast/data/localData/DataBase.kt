package com.example.weatherforecast.data.localData

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforecast.model.Weather

@Database(entities = arrayOf(Weather::class), version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class DataBase : RoomDatabase() {
    abstract fun dao(): Dao
    companion object {
        @Volatile
        private var INSTANCE: DataBase? = null
        fun getDatabase(application: Application): DataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    application.applicationContext, DataBase::class.java, "weather_database").build()
                INSTANCE = instance
                instance
            }
        }
    }
}