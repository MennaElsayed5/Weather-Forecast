package com.example.weatherforecast.ui.alerts.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherforecast.R
import com.example.weatherforecast.ui.alerts.viewModel.AlertsViewModel

class AlertsFragment : Fragment() {

    companion object {
        fun newInstance() = AlertsFragment()
    }

    private lateinit var viewModel: AlertsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.alerts_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AlertsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}