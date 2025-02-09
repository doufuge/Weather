package com.johny.weather.screen.weather

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.databinding.BindingAdapter
import com.johny.weather.model.WeatherItem
import com.johny.weather.utils.TimeUtil
import com.johny.weather.utils.dp

class LineChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var data: List<WeatherItem> = emptyList()

    private val startX = 100f
    private val startY = 700f
    private val xStep = 60F
    private var scaleFactor = 1f
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var offsetX = 0f
    private var offsetY = 0f

    private val axisPaint = Paint().apply {
        color = 0xFFB2B2B2.toInt()
        strokeWidth = 1.dp
        isAntiAlias = true
    }

    private val pathPaint = Paint().apply {
        color = Color.CYAN
        strokeWidth = 1.dp
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val xLabelPaint: Paint = Paint().apply {
        color = Color.BLUE
        textSize = 12.dp
        isAntiAlias = true
    }

    private val yLabelPaint: Paint = Paint().apply {
        color = Color.RED
        textSize = 14.dp
        isAntiAlias = true
    }

    private val scaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = scaleFactor.coerceIn(0.5f, 3f)
            invalidate()
            return true
        }
    })

    fun fillData(data: List<WeatherItem>) {
        this.data = data
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.scale(scaleFactor, scaleFactor, width / 2f, height / 2f)
        canvas.translate(offsetX, offsetY)
        drawAxis(canvas)
        drawYLabels(canvas)
        drawXLabels(canvas)
        drawPath(canvas)
        canvas.restore()
    }

    private fun drawAxis(canvas: Canvas) {
        val height = 600F
        val width = data.size * xStep
        canvas.drawLine(startX, startY, startX + width, startY, axisPaint)
        canvas.drawLine(startX, startY, startX, startY - height, axisPaint)
        canvas.save()
    }

    private fun drawXLabels(canvas: Canvas) {
        data.forEachIndexed { pos, weatherItem ->
            canvas.drawText(TimeUtil.formatHour(weatherItem.hour), startX + pos * xStep, startY + 50, xLabelPaint)
        }
        canvas.save()
    }

    private fun drawYLabels(canvas: Canvas) {
        repeat(4) { index ->
            canvas.drawText((index * 10).toString(), startX - xStep, startY - index * 200, yLabelPaint)
        }
        canvas.save()
    }

    private fun drawPath(canvas: Canvas) {
        if (data.isNotEmpty()) {
            val path = Path()
            path.moveTo(startX, startY - data.first().temp * 20)
            data.forEachIndexed { pos, weatherItem ->
                path.lineTo(startX + pos * xStep, startY - weatherItem.temp * 20)
            }
            canvas.drawPath(path, pathPaint)
            canvas.save()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchX = event.x
                lastTouchY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                offsetX += event.x - lastTouchX
                offsetY += event.y - lastTouchY
                lastTouchX = event.x
                lastTouchY = event.y
                invalidate()
            }
        }
        return true
    }

}

@BindingAdapter("weatherData")
fun bindingWeatherData(lineChartView: LineChartView, weatherData: List<WeatherItem>) {
    lineChartView.fillData(weatherData)
}