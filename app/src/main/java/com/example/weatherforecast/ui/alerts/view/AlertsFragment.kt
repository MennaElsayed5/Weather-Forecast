package com.example.weatherforecast.ui.alerts.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.example.weatherforecast.databinding.AlertDailogBinding
import com.example.weatherforecast.databinding.AlertsFragmentBinding
import com.example.weatherforecast.manger.PeriodicWorkManager
import com.example.weatherforecast.model.Alarm
import com.example.weatherforecast.ui.alerts.viewModel.AlertsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AlertsFragment : Fragment() {
    private lateinit var viewModel: AlertsViewModel
    private lateinit var binding: AlertsFragmentBinding
    private lateinit var alertAdabter: AlertsAdapter
    private lateinit var bindingDialog: AlertDailogBinding
    private lateinit var dialog: Dialog
    private var calStart = Calendar.getInstance()
    private var calEnd = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AlertsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[AlertsViewModel::class.java]
        binding.floatingActionButton.setOnClickListener {
           var  alarmObj = Alarm(0, 0, 0, 0)
               checkDrawOverlayPermission()
               showDialog(alarmObj)
        }
        viewModel.getAlarm().observe(viewLifecycleOwner) {
            alertAdabter = AlertsAdapter(arrayListOf(), viewModel, requireContext())
            binding.recyclerView.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding.recyclerView.hasFixedSize()
            alertAdabter.updateAlarms(it)
            binding.recyclerView.adapter = alertAdabter
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkDrawOverlayPermission() {
        if (!Settings.canDrawOverlays(requireContext())) {
            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle("need your permission")
                .setMessage("provide permission")
                .setPositiveButton("yes") { dialog: DialogInterface, _: Int ->
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + requireContext().applicationContext.packageName)
                    )
                    startActivityForResult(
                        intent, 1
                    )
                   dialog.dismiss()
                    var  alarmObj = Alarm(0, 0, 0, 0)
                    showDialog(alarmObj)
                }.setNegativeButton("cancel") { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                    var  alarmObj = Alarm(0, 0, 0, 0)
                    showDialog(alarmObj)
                }.show()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[AlertsViewModel::class.java]
        // TODO: Use the ViewModel
    }

    private fun showDialog(alarmObj: Alarm) {
        dialog = Dialog(requireContext())
        dialog.setCancelable(false)
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.getWindow()?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        bindingDialog = AlertDailogBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        var current = Calendar.getInstance()

        bindingDialog.startTime.setOnClickListener {
            val tpd1 = TimePickerDialog(
                context,
                { view, h, m ->
                    calStart.set(Calendar.HOUR_OF_DAY, h)
                    calStart.set(Calendar.MINUTE, m)
                    var time = (TimeUnit.MINUTES.toSeconds(m.toLong()) + TimeUnit.HOURS.toSeconds(
                        h.toLong()
                    ))
                    time = time.minus(3600L * 2)
                    alarmObj.start = time
                    bindingDialog.startTime.text = convertToTime(time, requireContext())

                },
                current.get(Calendar.HOUR_OF_DAY),
                current.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(context?.applicationContext)
            )
            tpd1.show()
        }
        bindingDialog.endTime.setOnClickListener {

            val t2 = TimePickerDialog(
                context,
                { _, h, m ->
                    calEnd.set(Calendar.HOUR_OF_DAY, h)
                    calEnd.set(Calendar.MINUTE, m)
                    var time = (TimeUnit.MINUTES.toSeconds(m.toLong()) + TimeUnit.HOURS.toSeconds(
                        h.toLong()
                    ))
                    time = time.minus(3600L * 2)
                    alarmObj.end = time
                    bindingDialog.endTime.text = convertToTime(time, requireContext())


                },
                current.get(Calendar.HOUR_OF_DAY),
                current.get(Calendar.MINUTE),
                android.text.format.DateFormat.is24HourFormat(context?.applicationContext)
            )
            t2.show()
        }

        bindingDialog.calenderBtn.setOnClickListener {
            val dateSetListener = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    calStart.set(Calendar.YEAR, year)
                    calStart.set(Calendar.MONTH, monthOfYear)
                    calStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    calEnd.set(Calendar.YEAR, year)
                    calEnd.set(Calendar.MONTH, monthOfYear)
                    calEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val date = "$dayOfMonth/${monthOfYear + 1}/$year"
                    val datalong = getDateMillis(date)
                    alarmObj.Date = datalong
                    bindingDialog.calenderBtn.text = convertToDate(datalong, requireContext())


                },
                current.get(Calendar.YEAR),
                current.get(Calendar.MONTH),
                current.get(Calendar.DAY_OF_MONTH)
            )

            dateSetListener.show()
        }
        bindingDialog.calender2Btn.setOnClickListener {
            val dateSetListener = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    calStart.set(Calendar.YEAR, year)
                    calStart.set(Calendar.MONTH, monthOfYear)
                    calStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    calEnd.set(Calendar.YEAR, year)
                    calEnd.set(Calendar.MONTH, monthOfYear)
                    calEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val date = "$dayOfMonth/${monthOfYear + 1}/$year"
                    val datalong = getDateMillis(date)
                    alarmObj.endData = datalong
                    bindingDialog.calender2Btn.text = convertToDate(datalong, requireContext())

                },
                current.get(Calendar.YEAR),
                current.get(Calendar.MONTH),
                current.get(Calendar.DAY_OF_MONTH)
            )

            dateSetListener.show()
        }
        bindingDialog.addAlarmBtn.setOnClickListener {
            viewModel.insertAlarm(alarmObj)
            dialog.cancel()

        }
        viewModel.id.observe(viewLifecycleOwner) {
            if (it != null) {
                setPeriodicWorkManager(it.toInt())
            }
        }

        bindingDialog.closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.window?.attributes = lp
    }

    private fun setPeriodicWorkManager(toInt: Int) {
        val data = Data.Builder()
        data.putInt("id", toInt)
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            PeriodicWorkManager::class.java,
            24, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            "$id",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )
    }

    private fun getDateMillis(date: String): Long {
        val f = SimpleDateFormat("dd/MM/yyyy")
        val d: Date = f.parse(date)
        return (d.time).div(1000)
    }

    fun convertToTime(dt: Long, context: Context): String {
        val date = Date(dt * 1000)
        val format = SimpleDateFormat("h:mm a")
        return format.format(date)
    }

    fun convertToDate(dt: Long, context: Context): String {
        val date = Date(dt * 1000)
        val format = SimpleDateFormat("d MMM, yyyy")
        return format.format(date)
    }

    private fun validateDialog(): Boolean {
        if (bindingDialog.fromTime.text.isEmpty())
            Toast.makeText(context, "time is empty", Toast.LENGTH_SHORT).show()
        else if (bindingDialog.calenderBtn.text.isEmpty())
            Toast.makeText(context, "Data is empty", Toast.LENGTH_SHORT).show()
        else
            return true
        return false
    }


}