package com.example.weatherforecast.ui.settings.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.Util.GPSLocation
import com.example.weatherforecast.databinding.FragmentSettingsBinding
import com.example.weatherforecast.ui.settings.viewModel.SettingsViewModel
import java.util.*

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var settingViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = requireActivity().getSharedPreferences("weather", Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        settingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(
            SettingsViewModel::class.java
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lang = sharedPref.getString("lang", "en")
        val unit = sharedPref.getString("units", "metric")
        val lat = sharedPref.getString("lat", "0").toString()
        val lon = sharedPref.getString("lon", "0").toString()
//        var location = sharedPref.getString("location", "GPS")
        when (lang) {
            "en" -> binding.radioButtonEn.isChecked = true
            "ar" -> binding.radioButtonAr.isChecked = true
        }
        when (unit) {
            "metric" -> binding.radioButtonC.isChecked = true
            "imperial" -> binding.radioButtonF.isChecked = true
            "standard" -> binding.radioButtonK.isChecked = true
        }
//        if (unit == "metric") {
//            binding.radioButtonC.isChecked = true
//            binding.radioButtonF.isChecked = false
//            binding.radioButtonK.isChecked = false
//        } else if (unit == "imperial") {
//            binding.radioButtonF.isChecked = true
//            binding.radioButtonC.isChecked = false
//            binding.radioButtonK.isChecked = false
//
//        } else {
//            binding.radioButtonK.isChecked = true
//            binding.radioButtonF.isChecked = false
//            binding.radioButtonC.isChecked = false
//        }

        if (lat == "0" && lon == "0") {
            binding.radioButtonGps.isChecked = true

        } else {
            binding.radioButtonMap.isChecked = true
        }
        binding.radioButtonEn.setOnClickListener {
            changeLang("en")
        }
        binding.radioButtonAr.setOnClickListener {
            changeLang("ar")
        }
        binding.radioButtonC.setOnClickListener {
//            binding.radioButtonF.isChecked = false
//            binding.radioButtonK.isChecked = false
            changeUnit("metric")

        }
        binding.radioButtonK.setOnClickListener {
            changeUnit("standard")
//            binding.radioButtonC.isChecked = false
//            binding.radioButtonF.isChecked = false
        }
        binding.radioButtonF.setOnClickListener {
            changeUnit("imperial")
//            binding.radioButtonK.isChecked = false
//            binding.radioButtonC.isChecked = false
        }
        binding.radioButtonMap.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(SettingsFragmentDirections.actionSettingsFragmentToMapFragment())
            Toast.makeText(requireContext(), "from settings Map", Toast.LENGTH_LONG).show()

        }
        binding.radioButtonGps.setOnClickListener {
            editor.putString("lat", "0")
            editor.putString("lon", "0")
            editor.apply()
            Navigation.findNavController(view)
                .navigate(SettingsFragmentDirections.actionSettingsFragmentToWeatherFragment())
            Toast.makeText(requireContext(), "enable gps", Toast.LENGTH_LONG).show()
        }

    }


    private fun changeLang(lang: String) {
        editor.putString("lang", lang)
        editor.apply()
//        settingViewModel.refreshData()
        setLocale(lang)
        restartApp()
    }

    private fun changeUnit(unit: String) {
        editor.putString("units", unit)
        editor.apply()
//        settingViewModel.refreshData()
        restartApp()
    }

    private fun restartApp() {

        startActivity(requireActivity().intent)
        requireActivity().finish()
    }

    private fun setLocale(languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = requireActivity().resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}