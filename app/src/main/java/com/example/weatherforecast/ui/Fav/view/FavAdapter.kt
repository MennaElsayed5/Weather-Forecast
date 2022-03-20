package com.example.weatherforecast.ui.Fav.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.CustomRowFavBinding
import com.example.weatherforecast.model.WeatherRespond
import com.example.weatherforecast.ui.Fav.viewModel.FavViewModel
import java.util.*

class FavAdapter(
    private var FavList: ArrayList<WeatherRespond>,
    favoritesViewModel: FavViewModel,
    context: Context
) :
    RecyclerView.Adapter<FavAdapter.FavViewHolder>() {
    private var favViewModel: FavViewModel
    private var context: Context
    private lateinit var navController: NavController
    lateinit var viewBinding:CustomRowFavBinding
     var sharedPreferences: SharedPreferences
     var lang: String

    init {
        this.favViewModel = favoritesViewModel
        this.context = context
        sharedPreferences = context.getSharedPreferences("weather", Context.MODE_PRIVATE)
        lang = sharedPreferences.getString("lang", "en").toString()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFav(newList: List<WeatherRespond>) {
        FavList.clear()
        FavList.addAll(newList)
        notifyDataSetChanged()
    }

    class FavViewHolder(var myView: CustomRowFavBinding) : RecyclerView.ViewHolder(myView.root)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
         viewBinding =
            CustomRowFavBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
//        navController = Navigation.findNavController()
        holder.myView.timezoneTxt.text = getCityText(FavList[position].lat, FavList[position].lon)
        holder.myView.deleteIcon.setOnClickListener {
            favViewModel.deleteFromFav(
                FavList[position].lat.toString(),
                FavList[position].lon.toString()
            )
        }
        holder.myView.favItemCard.setOnClickListener {
            val bundle = bundleOf( "lat" to  FavList[position].lat, "lon" to FavList[position].lon)
            navController=Navigation.findNavController(it)
            navController.navigate(R.id.action_favFragment_to_favDetailsFragment,bundle)
        }
    }

    private fun getCityText(lat: Double, lon: Double): String {
        if(lang=="en")
        {
            var city = "Unknown!"
            val geocoder = Geocoder(context, Locale("en"))
            val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
            if (addresses.isNotEmpty()) {
                val state = addresses[0].adminArea
                val country = addresses[0].countryName
                city = "$state, $country"
            }
            return city
        }
        else{
            var city = "Unknown!"
            val geocoder = Geocoder(context, Locale("ar"))
            val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
            if (addresses.isNotEmpty()) {
                val state = addresses[0].adminArea
                val country = addresses[0].countryName
                city = "$state, $country"
            }
            return city
        }

    }

    override fun getItemCount() = FavList.size
}