package com.example.weatherforecast.ui.weather.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.Util.Helper
import com.example.weatherforecast.databinding.FragmentFavDetailsBinding
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.WeatherRespond
import com.example.weatherforecast.ui.weather.viewModel.FavDetailsViewModel
import com.example.weatherforecast.ui.weather.viewModel.FavViewModelFactory
import java.util.*

class FavDetailsFragment : Fragment() {
    private lateinit var binding: FragmentFavDetailsBinding
    val viewModel: FavDetailsViewModel by viewModels<FavDetailsViewModel> {
        FavViewModelFactory(Repository.getRepoInstance(requireActivity().application))
    }
    private val dayAdapter = FavDayAdater(arrayListOf())
    private val weekAdapter = FavWeekAdapter(arrayListOf())
    var livedata = MutableLiveData<WeatherRespond>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    lateinit var lat: String
    lateinit var lon: String
    lateinit var lang: String
    private lateinit var unit: String
    private lateinit var tempUnit: String
    private lateinit var windSpeedUnit: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("weather", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        lat = sharedPreferences.getString("lat", "0").toString()
        lon = sharedPreferences.getString("lat", "0").toString()
        lang = sharedPreferences.getString("lang", "en").toString()
        unit = sharedPreferences.getString("units", "metric").toString()
        setLocale(lang)
        setUnits(unit)
        editor.commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lat = requireArguments().getDouble("lat")
        val lon = requireArguments().getDouble("lon")
        if (Helper.isNetworkAvailable(requireContext())) {
            viewModel.insertWeatherData(
                lat.toString(), lon.toString(),
                "minutely",
                unit,
                lang
            )
        }
        viewModel.liveDataWeather.observe(viewLifecycleOwner) {
            binding.cityName.text = getCityText(
                lat, lon
            )
            binding.txtDesc.text = it.current.weather[0].description
            binding.iconFeelLike.setImageResource(getIcon(it.current.weather[0].icon))
            if(lang == "en") {
                binding.tempTxt.text = it.current.temp.toInt().toString() + "°"
                binding.txtCloud.text = it.current.clouds.toString()
                binding.txtWind.text = it.current.wind_speed.toString()
                binding.txtPressure.text = it.current.pressure.toString()
                binding.txtHumidity.text = it.current.humidity.toString()
            }
            else{
                binding.tempTxt.text = convertToArabic(it.current.temp.toInt())+"°"
                binding.txtCloud.text =convertToArabic( it.current.clouds)
                binding.txtWind.text = convertToArabic(it.current.wind_speed.toInt())
                binding.txtPressure.text = convertToArabic(it.current.pressure)
                binding.txtHumidity.text =convertToArabic(it.current.humidity)
            }

            binding.rcItemDay.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            binding.rcItemDay.hasFixedSize()
            dayAdapter.updateDay(it.hourly)
            binding.rcItemDay.adapter = dayAdapter

            binding.rcItemWeek.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding.rcItemWeek.hasFixedSize()
            weekAdapter.updateWeek(it.daily)
            binding.rcItemWeek.adapter = weekAdapter


        }
    }

    private fun getIcon(icon: String): Int {
        when (icon) {
            "01n" -> {
                return R.drawable.a01n
            }
            "02n" -> {
                return R.drawable.a02n
            }
            "03n" -> {
                return R.drawable.a03n
            }
            "04n" -> {
                return R.drawable.a04n
            }
            "09n" -> {
                return R.drawable.a09n
            }
            "10n" -> {
                return R.drawable.a10n
            }
            "11n" -> {
                return R.drawable.a11n
            }
            "13n" -> {
                return R.drawable.a13n
            }
            "01d" -> {
                return R.drawable.a01d
            }
            "02d" -> {
                return R.drawable.a02d
            }
            "03d" -> {
                return R.drawable.a03d
            }
            "04d" -> {
                return R.drawable.a04d
            }
            "09d" -> {
                return R.drawable.a09d
            }
            "10d" -> {
                return R.drawable.a10d
            }
            "11d" -> {
                return R.drawable.a11d
            }
            "13d" -> {
                return R.drawable.a13d
            }
            else -> {
                return R.drawable.a01n
            }
        }

    }

    private fun getCityText(lat: Double, lon: Double): String {
        if (lang == "en") {
            var city = "Unknown!"
            val geocoder = Geocoder(requireContext(), Locale("en"))
            val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
            if (addresses.isNotEmpty()) {
                val state = addresses[0].adminArea
                val country = addresses[0].countryName
                city = "$state, $country"
            }
            return city
        } else {
            var city = "Unknown!"
            val geocoder = Geocoder(requireContext(), Locale("ar"))
            val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
            if (addresses.isNotEmpty()) {
                val state = addresses[0].adminArea
                val country = addresses[0].countryName
                city = "$state, $country"
            }
            return city
        }

    }

    private fun setUnits(unit: String) {
        when (unit) {
            "metric" -> {
                tempUnit = "°c"
                windSpeedUnit = "m/s"
            }
            "imperial" -> {
                tempUnit = "°f"
                windSpeedUnit = "m/h"
            }
            "standard" -> {
                tempUnit = "°k"
                windSpeedUnit = "m/s"
            }

        }
    }


    private fun setLocale(languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = requireActivity().resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
    private fun convertToArabic(value: Int): String {
        return (value.toString() + "")
            .replace("1", "١").replace("2", "٢")
            .replace("3", "٣").replace("4", "٤")
            .replace("5", "٥").replace("6", "٦")
            .replace("7", "٧").replace("8", "٨")
            .replace("9", "٩").replace("0", "٠")
    }
}