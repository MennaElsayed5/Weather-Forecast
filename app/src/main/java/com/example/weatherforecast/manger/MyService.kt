package com.example.weatherforecast.manger

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.example.weatherforecast.Util.Notfications

class MyService : Service() {

    val notifications: Notfications = Notfications(this)
    override fun onBind(intent: Intent): IBinder {
        return null!!
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        var des = intent!!.getStringExtra("description")
      val notifi=  notifications.createNotification(des, "weather forecast")
        startForeground(88,notifi)
        if (Settings.canDrawOverlays(this)) {
            val myWindowManager: MyWindowManager = MyWindowManager(this, des!!)
            myWindowManager.startWindowManager()
        }
        return START_NOT_STICKY


    }
}