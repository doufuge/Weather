package com.johny.weather.utils

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("layout_height")
fun setViewHeight(view: View, height: Int = 0) {
    view.layoutParams.apply {
        this.height = height
    }.run {
        view.layoutParams = this
    }
}

@BindingAdapter("layout_width")
fun setViewWidth(view: View, width: Int = 0) {
    view.layoutParams.apply {
        this.width = width
    }.run {
        view.layoutParams = this
    }
}