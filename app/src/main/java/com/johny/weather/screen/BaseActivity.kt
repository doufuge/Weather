package com.johny.weather.screen

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<BINDING: ViewDataBinding, VM: ViewModel> : AppCompatActivity() {

    protected lateinit var binding: BINDING
    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        val currentUiMode = resources.configuration.uiMode
        val nightMode = currentUiMode and Configuration.UI_MODE_NIGHT_MASK
        fitScreen(nightMode == Configuration.UI_MODE_NIGHT_YES)
        setup()
    }

    protected fun fitScreen(nightMode: Boolean) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                if (nightMode) 0 else WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                if (nightMode) 0 else WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            window.decorView.systemUiVisibility =
                if (nightMode) 0 else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    protected open fun setup() {}

    @Suppress("UNCHECKED_CAST")
    private fun performDataBinding() {
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0].let { bindingType ->
            (bindingType as Class<*>).let { bindClazz ->
                bindClazz.getDeclaredMethod("inflate", LayoutInflater::class.java).let { method ->
                    binding = method.invoke(null, layoutInflater) as BINDING
                    binding.lifecycleOwner = this
                    setContentView(binding.root)
                }
            }
        }

        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1].let { vmType ->
            (vmType as Class<ViewModel>).let { vmClazz ->
                viewModel = ViewModelProvider(this)[vmClazz] as VM
            }
        }
    }

}