package com.example.weatherforecast.ui.alerts.view

import android.app.*
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.example.weatherforecast.R
import com.example.weatherforecast.Util.Notfications
import com.example.weatherforecast.databinding.CustomRowAlertsBinding
import com.example.weatherforecast.model.Alarm
import com.example.weatherforecast.model.Alerts
import com.example.weatherforecast.model.Daily
import com.example.weatherforecast.ui.alerts.viewModel.AlertsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AlertsAdapter(var alarmList: ArrayList<Alarm>, alartViewModel: AlertsViewModel, context: Context): RecyclerView.Adapter<AlertsAdapter.VH>() {
    var context: Context
    var alartViewModel: AlertsViewModel
    var notificationUtils: Notfications
    init {
        this.context = context
        this.alartViewModel = alartViewModel
        notificationUtils = Notfications(context)
    }
    fun updateAlarms(newAlarmList: List<Alarm>) {
        alarmList.clear()
        alarmList.addAll(newAlarmList)
        notifyDataSetChanged()
    }


    class VH (var myView: CustomRowAlertsBinding): RecyclerView.ViewHolder(myView.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val viewBinding =
            CustomRowAlertsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(viewBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.myView.start.text =convertToTime(alarmList[position].start,context)
        holder.myView.data.text =convertToDate(alarmList[position].Date,context)
        holder.myView.deleteBtn.setOnClickListener {
            alartViewModel.deleteAlarm(alarmList[position])
        }

    }

    fun convertToTime(dt: Long, context: Context): String {
        val date = Date(dt * 1000)
        val format = SimpleDateFormat("h:mm a")
        return format.format(date)
    }

    fun convertToDate(dt: Long, context: Context): String {
        val date = Date(dt * 1000)
        val format = SimpleDateFormat("d MMM, yyyy")
        return format.format(date)
    }



    override fun getItemCount() = alarmList.size
}