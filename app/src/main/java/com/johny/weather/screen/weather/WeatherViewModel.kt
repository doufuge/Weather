package com.johny.weather.screen.weather

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johny.weather.data.api.WeatherApi
import com.johny.weather.model.WeatherItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherApi: WeatherApi
) : ViewModel() {

    companion object {
        const val DEFAULT_LATITUDE = 52.52
        const val DEFAULT_LONGITUDE = 13.41
    }

    var showTable = false
        private set

    private val _uiEvent = MutableLiveData<WeatherUiEvent>()
    val uiEvent: LiveData<WeatherUiEvent> get() = _uiEvent
    private val _tip = MutableLiveData<String>()
    val tip: LiveData<String> get() = _tip
    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> get() = _loading
    private val _weatherData = MutableLiveData<List<WeatherItem>>(emptyList())
    val weatherData: LiveData<List<WeatherItem>> get() = _weatherData

    private var fetchJob: Job? = null

    private var latitude = DEFAULT_LATITUDE
    private var longitude = DEFAULT_LONGITUDE

    fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude
        this.fetchWeather()
    }

    fun toggleViewMode() {
        showTable = !showTable
        _uiEvent.value = WeatherUiEvent.ChangeViewMode(showTable)
    }

    fun reload() {
        _uiEvent.value = WeatherUiEvent.Reload
    }

    fun showTip(tipStr: String, autoHide: Boolean = true) {
        _tip.value = tipStr
        _uiEvent.value = WeatherUiEvent.ShowTip(autoHide)
    }

    fun onLocateFailed(tipStr: String) {
        showTip(tipStr, false)
        _loading.value = false
    }

    private fun fetchWeather() {
        fetchJob?.cancel()
        _loading.value = true
        fetchJob = viewModelScope.launch {
            try {
                weatherApi.fetchWeather(latitude, longitude).let { resp ->
                    val tempUnit = resp.hourly_units.temperature_2m
                    if (resp.hourly.time.size == resp.hourly.temperature_2m.size) {
                        _weatherData.value = resp.hourly.time.mapIndexed { index, time ->
                            WeatherItem(time, resp.hourly.temperature_2m[index], tempUnit)
                        }
                    }
                    _loading.value = false
                    _uiEvent.value = WeatherUiEvent.DataLoaded
                }
            } catch (e: Exception) {
                _loading.value = false
                _tip.value = "error: ${e.message}"
                _uiEvent.value = WeatherUiEvent.ShowTip(true)
            }
        }
    }


}