package com.example.weatherforecast.ui.weather.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.R
import com.example.weatherforecast.model.Daily
import java.util.*
import kotlin.collections.ArrayList

class WeekAdapter(var WeeklyList: ArrayList<Daily>) :
    RecyclerView.Adapter<WeekAdapter.WeeklyViewHolder>() {


        fun updateWeek(newDailyList: List<Daily>) {
        WeeklyList.clear()
        WeeklyList.addAll(newDailyList)
        notifyDataSetChanged()
    }

    class WeeklyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtWeather: TextView = itemView.findViewById(R.id.txt_weather)
        val txt_day: TextView = itemView.findViewById(R.id.txt_day)
        val txt_mindegree: TextView = itemView.findViewById(R.id.txt_mindegree)
        val img: ImageView = itemView.findViewById(R.id.img_weather)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_row_7days, parent, false)
        return WeeklyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WeeklyViewHolder, position: Int) {
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(WeeklyList[position].dt.toLong() * 1000)
        var date = calendar.get(Calendar.DAY_OF_MONTH)
            .toString() + "/" + (calendar.get(Calendar.MONTH) + 1).toString()
        holder.txtWeather.text = WeeklyList[position].weather.get(0).description
        holder.txt_mindegree.text =
            (WeeklyList[position].temp.min.toInt()).toString() + "°" + "/" + (WeeklyList[position].temp.max.toInt()).toString() + "°"
        holder.txt_day.text = dayFormat(WeeklyList[position].dt.toInt())
        Glide.with(holder.img.context)
            .load(iconLinkgetter(WeeklyList[position].weather.get(0).icon))
            .placeholder(R.drawable.ic_wb_sunny_black_24dp).into(holder.img)


    }

    override fun getItemCount() = WeeklyList.size

    fun iconLinkgetter(iconName: String): String =
        "https://openweathermap.org/img/wn/" + iconName + "@2x.png"

    private fun dayFormat(milliSecond: Int): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSecond.toLong() * 1000)
        var date = calendar.get(Calendar.DAY_OF_MONTH)
            .toString() + "/" + (calendar.get(Calendar.MONTH) + 1).toString()
        var day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
            .toString()
        return day
    }

}