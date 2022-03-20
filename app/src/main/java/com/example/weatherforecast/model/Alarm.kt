package com.example.weatherforecast.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Alarms")
class Alarm(
    var Date: Long,
    var endData:Long,
    var start: Long,
    var end: Long,

    )
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}