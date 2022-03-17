package com.example.weatherforecast.ui.settings.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentSettingsBinding
import com.example.weatherforecast.ui.settings.viewModel.SettingsViewModel
import java.util.*

class SettingsFragment : Fragment() {
    lateinit var binding:FragmentSettingsBinding
    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var settingViewModel:SettingsViewModel
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
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var lang=sharedPref.getString("lang","en")
        var unit=sharedPref.getString("units","metric")
        when (lang) {
            "en" -> binding.radioButtonEn.isChecked=true
            "ar" -> binding.radioButtonAr.isChecked=true
        }
        when (unit) {
            "metric" -> binding.radioButtonC.isChecked=true
            "imperial" -> binding.radioButtonF.isChecked=true
            "standard" -> binding.radioButtonK.isChecked=true
        }
        binding.radioButtonEn.setOnClickListener(View.OnClickListener {
            changeLang("en")
        })
        binding.radioButtonAr.setOnClickListener(View.OnClickListener {
            changeLang("ar")
        })
        binding.radioButtonC.setOnClickListener(View.OnClickListener {
            changeUnit("metric")
        })
        binding.radioButtonK.setOnClickListener(View.OnClickListener {
            changeUnit("standard")
        })
        binding.radioButtonF.setOnClickListener(View.OnClickListener {
            changeUnit("imperial")
        })



    }
    companion object {

    }

    private fun changeLang(lang:String){
        editor.putString("lang",lang)
        editor.commit()
        settingViewModel.refreshData()
        setLocale(lang)
        restartApp()
    }
    private fun changeUnit(unit:String){
        editor.putString("units",unit)
        editor.commit()
        settingViewModel.refreshData()
        restartApp()
    }
    private fun restartApp()
    {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        Runtime.getRuntime().exit(0)

    }
    fun setLocale(languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = requireActivity().resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}