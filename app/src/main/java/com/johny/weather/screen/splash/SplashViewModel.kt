package com.johny.weather.screen.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    val uiEvent = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            delay(600)
            uiEvent.postValue("weather")
        }
    }

}
