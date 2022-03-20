package com.example.weatherforecast.ui.Fav.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentMapsBinding
import com.example.weatherforecast.ui.Fav.viewModel.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {
    var lat: String = "0.0"
    var lon: String = "0.0"
    private lateinit var binding: FragmentMapsBinding
    private lateinit var viewModel: MapViewModel
    private lateinit var navController: NavController
    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor


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
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dir: Boolean = requireArguments().getBoolean("map")
        navController = Navigation.findNavController(view)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        sharedPref = requireActivity().getSharedPreferences("weather", Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[MapViewModel::class.java]
        binding.saveBtn.setOnClickListener {
            viewModel.setDataLocation(lat, lon)
            editor.putString("lat", lat)
            editor.putString("lon", lon)
            editor.commit()
            if (dir) {
                navController.navigate(MapsFragmentDirections.actionMapFragmentToFavFragment())
                Toast.makeText(requireContext(), "from fav", Toast.LENGTH_LONG).show()
            } else {
                navController.navigate(MapsFragmentDirections.actionMapFragmentToWeatherFragment())
                Toast.makeText(requireContext(), "from settings", Toast.LENGTH_LONG).show()
            }
        }


    }
}