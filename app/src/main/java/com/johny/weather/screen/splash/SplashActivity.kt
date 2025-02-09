package com.johny.weather.screen.splash

import android.annotation.SuppressLint
import android.content.Intent
import com.johny.weather.databinding.ActivitySplashBinding
import com.johny.weather.screen.BaseActivity
import com.johny.weather.screen.weather.WeatherActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    override fun setup() {
        viewModel.uiEvent.observe(this) { event ->
            if ("weather" == event) {
                startActivity(Intent(this, WeatherActivity::class.java))
                finish()
            }
        }
    }

}