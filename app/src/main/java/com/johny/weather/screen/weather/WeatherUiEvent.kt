package com.johny.weather.screen.weather

sealed class WeatherUiEvent {

    data class ChangeViewMode(val showTable: Boolean): WeatherUiEvent()
    data object DataLoaded: WeatherUiEvent()
    data object Reload: WeatherUiEvent()
    data class ShowTip(val autoHide: Boolean = false): WeatherUiEvent()

}