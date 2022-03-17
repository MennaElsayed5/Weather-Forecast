package com.example.weatherforecast.ui.Fav.view

import android.content.Context
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentMapsBinding
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.ui.Fav.viewModel.MapViewModel
import com.example.weatherforecast.ui.weather.viewModel.ViewModelFactory
import com.example.weatherforecast.ui.weather.viewModel.WeatherViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {
    lateinit var lat: String
    lateinit var lon: String
    lateinit var binding: FragmentMapsBinding
    private lateinit var viewModel: MapViewModel
    private lateinit var navController: NavController


    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(lat.toDouble(), lon.toDouble())
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        googleMap.setOnMapClickListener {
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(it))
            lat = it.latitude.toString()
            lon = it.longitude.toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        val sharedPreferences =
            requireActivity().application.getSharedPreferences("weather", Context.MODE_PRIVATE)
        lat = sharedPreferences.getString("lat", "30.5965").toString()
        lon = sharedPreferences.getString("lon", "30.5965").toString()
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(MapViewModel::class.java)
        navController = Navigation.findNavController(view)
        binding.saveBtn.setOnClickListener {
            viewModel.setDataLocation(lat, lon)
            Log.i("TAG", "onViewCreated: " + lat + lon)
            navController.navigate(MapsFragmentDirections.actionMapFragmentToFavFragment())
        }


    }
}