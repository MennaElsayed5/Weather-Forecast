package com.example.weatherforecast.Util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.ContextWrapper
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.weatherforecast.R

class Notfications (context: Context):ContextWrapper(context) {
    var context:Context = context
    fun createNotification(body: String?, title: String?):Notification {
        val notificationManager = context
            .getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(
            context, "WEATHER_CHANNEL")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/" + R.raw.weather_alert))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "gfhfg"
            val description = "awds"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("WEATHER_CHANNEL1", name, importance)
            channel.description = description
            builder.setChannelId("WEATHER_CHANNEL1")
            notificationManager.createNotificationChannel(channel)
        }
        return builder.build()
    }


}