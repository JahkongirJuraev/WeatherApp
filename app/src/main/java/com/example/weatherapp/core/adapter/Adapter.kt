package com.example.weatherapp.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.core.model.FiveDaysWeatherResponse.MoreData
import com.example.weatherapp.databinding.ItemListBinding

class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>() {

    var data = ArrayList<MoreData>()

    fun setData(data: List<MoreData>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(respond: MoreData) {

            var fullDate = respond.dt_txt.split(" ")
            var day = fullDate[0]
            var fullTime = fullDate[1].split(":")
            binding.day.text = day
            binding.time.text = "${fullTime[0]}:${fullTime[1]}"
            respond.weather[0].main

            if (respond.weather[0].main == "Thunderstorm") {
                binding.weatherIcon.setImageResource(R.drawable.thunderstoneicon)
            } else if (respond.weather[0].main == "Drizzle") {
                binding.weatherIcon.setImageResource(R.drawable.heavyrainicon)
            } else if (respond.weather[0].main == "Rain") {
                binding.weatherIcon.setImageResource(R.drawable.rainicon)
            } else if (respond.weather[0].main == "Snow") {
                binding.weatherIcon.setImageResource(R.drawable.snowicon)
            } else if (respond.weather[0].id in 701..798) {
                binding.weatherIcon.setImageResource(R.drawable.misticon)
            } else if (respond.weather[0].main == "Clear") {
                binding.weatherIcon.setImageResource(R.drawable.clear_sky_icon)
            } else if (respond.weather[0].id == 801 || respond.weather.get(0).id == 802) {
                binding.weatherIcon.setImageResource(R.drawable.littlecloudicon)
            } else if (respond.weather[0].id == 803 || respond.weather.get(0).id == 804) {
                binding.weatherIcon.setImageResource(R.drawable.littlecloudicon)
            }

            val temp: Int = respond.main.feels_like.toInt()

            binding.degree.text = "$temp°"

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
