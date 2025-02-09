package com.johny.weather.screen.weather

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.johny.weather.databinding.CellWeatherBinding
import com.johny.weather.model.WeatherItem

class WeatherAdapter: RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private var data: List<WeatherItem> = emptyList()

    companion object {
        val BG_HIGHLIGHT = Color.argb(128, 236,204,156)
        val BG_NORMAL = Color.argb(24, 0, 0, 0)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fillData(data: List<WeatherItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder =
        WeatherViewHolder(CellWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.binding.item = data[position]
        holder.binding.root.setBackgroundColor(if (position % 2 ==0) BG_HIGHLIGHT else BG_NORMAL)
    }

    inner class WeatherViewHolder(
        val binding: CellWeatherBinding
    ): RecyclerView.ViewHolder(binding.root)

}

@BindingAdapter("weatherData")
fun bindingWeatherData(recyclerView: RecyclerView, weatherData: List<WeatherItem>) {
    recyclerView.adapter?.let { adapter ->
        if (adapter is WeatherAdapter) {
            adapter.fillData(weatherData)
        }
    }
}
