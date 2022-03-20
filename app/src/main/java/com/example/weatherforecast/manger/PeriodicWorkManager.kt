package com.example.weatherforecast.manger

import android.content.Context
import android.content.SharedPreferences
import androidx.work.*
import com.example.weatherforecast.model.Alarm
import com.example.weatherforecast.model.Repository
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PeriodicWorkManager(
    private val appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    var sharedPref:SharedPreferences= appContext.getSharedPreferences("weather", Context.MODE_PRIVATE)
    lateinit var lang: String
    lateinit var lat:String
    lateinit var lon:String
    lateinit var unit: String
    override suspend fun doWork(): Result {
        lang=sharedPref.getString("lang","en").toString()
        unit=sharedPref.getString("units","metric").toString()
        lat = sharedPref?.getString("lat", "0").toString()
        lon = sharedPref?.getString("lon", "0").toString()
        val repository: Repository = Repository.getRepoInstance(appContext)
        var weather=repository.getCurrentWeatherData(lat,lon,"minutely",unit,lang)
        var id = inputData.getInt("id", 0)
        var alert = repository.getAlarmId(id)
        if (checkTime(alert)) {
            val daly=getDelay(alert)
            setOneTimeWork(daly,alert.id,weather.current.weather[0].description)
        }
        return Result.success()

    }

    private fun setOneTimeWork(daly: Long, id: Int, description: String) {
        val data = Data.Builder()
        data.putString("description", description)
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
            OneTimeWork::class.java,
        )
            .setInitialDelay(daly, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance().enqueueUniqueWork(
            "$id",
            ExistingWorkPolicy.REPLACE,
            oneTimeWorkRequest
        )
    }


    private fun checkTime(alarm: Alarm): Boolean {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val date = "$day/${month + 1}/$year"
        val dayNow = getDateMillis(date)
        return dayNow >= alarm.Date && dayNow <= alarm.endData
    }
    private fun getDateMillis(date: String): Long {
        val f = SimpleDateFormat("dd/MM/yyyy")
        val d: Date = f.parse(date)
        return (d.time).div(1000)
    }
    private fun getDelay(alarm: Alarm): Long {
        val hour =
            TimeUnit.HOURS.toSeconds(Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toLong())
        val minute =
            TimeUnit.MINUTES.toSeconds(Calendar.getInstance().get(Calendar.MINUTE).toLong())
        return alarm.start - ((hour + minute) - (2 * 3600L))
    }

}