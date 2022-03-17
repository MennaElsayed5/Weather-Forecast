package com.example.weatherforecast.ui.Fav.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FavFragmentBinding
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.WeatherRespond
import com.example.weatherforecast.ui.Fav.viewModel.FavViewModel
import com.example.weatherforecast.ui.Fav.viewModel.MapViewModel
import com.example.weatherforecast.ui.weather.viewModel.ViewModelFactory
import com.example.weatherforecast.ui.weather.viewModel.WeatherViewModel


class FavFragment : Fragment() {
    private lateinit var binding: FavFragmentBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: FavViewModel

    var livedata = MutableLiveData<WeatherRespond>()

    companion object {
        fun newInstance() = FavFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(FavViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(FavViewModel::class.java)
        navController = Navigation.findNavController(view)

        binding.floatingActionButton.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_favFragment_to_mapFragment)

        })
        viewModel.getFavWeather().observe(viewLifecycleOwner) {
            var favAdapter = FavAdapter(arrayListOf(), viewModel, requireContext())
            binding.recyclerView.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding.recyclerView.hasFixedSize()
            favAdapter.updateFav(it)
            binding.recyclerView.adapter = favAdapter
        }


    }

}