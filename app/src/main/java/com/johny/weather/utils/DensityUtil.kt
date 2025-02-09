package com.johny.weather.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.WindowManager
import kotlin.math.ceil

class DensityUtil {

    companion object {
        @JvmField var statusBarHeight: Int = 0
        @JvmField var bottomBarHeight: Int = 0
        @JvmField var screenWidth: Int = 0
        @JvmField var screenHeight: Int = 0

        @SuppressLint("InternalInsetResource", "DiscouragedApi")
        @JvmStatic
        fun config(context: Context) {
            val sbhId = context.resources.getIdentifier("status_bar_height", "dimen", "android")

            statusBarHeight = if (sbhId > 0) {
                context.resources.getDimensionPixelSize(sbhId)
            } else {
                val density = context.resources.displayMetrics.density
                ceil(24.0 * density).toInt()
            }

            val nbhId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
            bottomBarHeight = if (nbhId > 0) {
                context.resources.getDimensionPixelSize(nbhId)
            } else {
                0
            }

            screenWidth = context.resources.displayMetrics.widthPixels
            screenHeight = context.resources.displayMetrics.heightPixels

        }
    }

}


val Float.dp: Float
    get() = this * Resources.getSystem().displayMetrics.density

val Float.toDp: Float
    get() = this / Resources.getSystem().displayMetrics.density

val Int.dp: Float
    get() = this.toFloat().dp

fun getScreenWidth(context: Context): Int =
    DisplayMetrics().run {
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).let { windowManager ->
            windowManager.defaultDisplay?.getMetrics(this)
            this.widthPixels
        }
    }

fun getScreenHeight(context: Context): Int =
    DisplayMetrics().run {
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).let { windowManager ->
            windowManager.defaultDisplay?.getMetrics(this)
            this.heightPixels
        }
    }

