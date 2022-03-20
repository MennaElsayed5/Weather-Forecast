package com.example.weatherforecast.ui.weather.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.R
import com.example.weatherforecast.model.Hourly
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DayAdapter(private var items: ArrayList<Hourly>) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateDay(newHourlyList: List<Hourly>) {
        items.clear()
        items.addAll(newHourlyList)
        notifyDataSetChanged()
    }
    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtWeather : TextView = itemView.findViewById(R.id.txt_weather)
        val txtTime : TextView = itemView.findViewById(R.id.txt_time)
        val img: ImageView = itemView.findViewById(R.id.img_weather)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_row_current_day,parent,  false)
        return DayViewHolder(view)
    }
    @SuppressLint("SimpleDateFormat")
    private fun timeFormat(millisSeconds:Int ): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = (millisSeconds * 1000).toLong()
        val format = SimpleDateFormat("hh:00 aaa")
        return format.format(calendar.time)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.txtWeather.text=items[position].temp.toInt().toString()+"°"
        holder.txtTime.text= timeFormat(items[position].dt.toInt())
        Glide.with(holder.img.context).load(iconLinkgetter(items[position].weather[0].icon))
            .placeholder(R.drawable.ic_wb_sunny_black_24dp).into(holder.img)

    }
    private fun iconLinkgetter(iconName:String):String=
        "https://openweathermap.org/img/wn/$iconName@2x.png"
    override fun getItemCount() =items.size
}