package com.example.weatherforecast.data.localData

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforecast.model.Alarm
import com.example.weatherforecast.model.WeatherRespond

@Database(entities =  [(WeatherRespond::class ), (Alarm::class)], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class DataBase : RoomDatabase() {
    abstract fun dao(): Dao
    companion object {
        @Volatile
        private var INSTANCE: DataBase? = null
        fun getDatabase(application: Application): DataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    application.applicationContext, DataBase::class.java, "weather").build()
                INSTANCE = instance
                instance
            }
        }
        fun getDatabase(context: Context): DataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, DataBase::class.java, "weather").build()
                INSTANCE = instance
                instance
            }
        }
    }
}