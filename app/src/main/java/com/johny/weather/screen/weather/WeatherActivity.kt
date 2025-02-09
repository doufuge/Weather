package com.johny.weather.screen.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.johny.weather.R
import com.johny.weather.databinding.ActivityWeatherBinding
import com.johny.weather.screen.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : BaseActivity<ActivityWeatherBinding, WeatherViewModel>(), LocationListener {

    private lateinit var locationManager: LocationManager
    private val adapter: WeatherAdapter by lazy {
        WeatherAdapter()
    }
    private val locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions.all { it.value }) {
            getLocation()
        } else {
            viewModel.showTip(ContextCompat.getString(this, R.string.no_permission_tip), false)
        }
    }

    override fun setup() {
        binding.vm = viewModel
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        startLocate()
        binding.recyclerView.apply {
            adapter = this@WeatherActivity.adapter
            layoutManager = LinearLayoutManager(this@WeatherActivity)
        }

        viewModel.uiEvent.observe(this) { uiEvent ->
            when(uiEvent) {
                is WeatherUiEvent.ChangeViewMode -> changeViewMode(uiEvent.showTable)
                is WeatherUiEvent.DataLoaded -> changeViewMode(viewModel.showTable)
                is WeatherUiEvent.ShowTip -> showTip(uiEvent.autoHide)
                is WeatherUiEvent.Reload -> startLocate()
            }
        }
    }

    private fun startLocate() {
        Log.i(TAG, "======= startLocate")
        startTipAnim(false)
        locationPermissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }

    private fun showTip(autoHide: Boolean = false) {
        binding.tipView.visibility = View.VISIBLE
        startTipAnim(true)
        if (autoHide) {
            binding.tipView.postDelayed({
                startTipAnim(false)
            }, 3000)
        }
    }

    private fun startTipAnim(show: Boolean) {
        if (binding.tipView.visibility != View.VISIBLE) {
            return
        }
        AnimationUtils.loadAnimation(this, if (show) R.anim.tip_show else R.anim.tip_hide).let { anim ->
            binding.tipView.startAnimation(anim)
            if (!show) {
                anim.setAnimationListener(object: AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        binding.tipView.visibility = View.GONE
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }
                })
            }
        }
    }

    private fun changeViewMode(showTable: Boolean) {
        Log.i(TAG, "changeViewMode showTable: $showTable")
        binding.btnViewMode.setImageResource(if (showTable) R.drawable.ic_line_chart else R.drawable.ic_list_chart)
        if (showTable) {
            binding.tableChartView.visibility = View.VISIBLE
            binding.lineChartView.visibility = View.GONE
            AnimationUtils.loadAnimation(this@WeatherActivity, R.anim.page_enter).let { anim ->
                binding.tableChartView.startAnimation(anim)
            }
            AnimationUtils.loadAnimation(this@WeatherActivity, R.anim.page_exit).let { anim ->
                binding.lineChartView.startAnimation(anim)
            }
        } else {
            binding.tableChartView.visibility = View.GONE
            binding.lineChartView.visibility = View.VISIBLE
            binding.lineChartView.fillData(viewModel.weatherData.value!!)
            AnimationUtils.loadAnimation(this@WeatherActivity, R.anim.page_enter).let { anim ->
                binding.lineChartView.startAnimation(anim)
            }
            AnimationUtils.loadAnimation(this@WeatherActivity, R.anim.page_exit).let { anim ->
                binding.tableChartView.startAnimation(anim)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGpsEnabled && !isNetworkEnabled) {
            viewModel.onLocateFailed(ContextCompat.getString(this, R.string.permission_request_tip))
            return
        }

        // Use network location first.
        when {
            isNetworkEnabled -> locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10f, this)
            isGpsEnabled -> locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, this)
        }
    }

    override fun onLocationChanged(location: Location) {
        viewModel.onLocationChanged(location)
        locationManager.removeUpdates(this)
    }

    companion object {
        private const val TAG = "WeatherActivity"
    }

}