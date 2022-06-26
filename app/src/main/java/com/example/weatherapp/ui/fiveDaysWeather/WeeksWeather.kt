package com.example.weatherapp.ui.fiveDaysWeather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.core.adapter.Adapter
import com.example.weatherapp.core.model.FiveDaysWeatherResponse.FiveDaysHourlyWeatherRespond
import com.example.weatherapp.databinding.WeeksWaetherFragmentBinding
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


class WeeksWeather : Fragment(), FiveDaysWeatherMVP.View {

    var presenter: FiveDaysWeatherMVP.Presenter? = null
    var adapter: Adapter? = null

    var _binding: WeeksWaetherFragmentBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = WeeksWaetherFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = FiveDaysWeatherPresenter(this)
        presenter!!.loadFiveDaysWeather()
        adapter = Adapter()
        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = layoutManager
    }


    override fun getFiveDaysWeather(fiveDaysHourlyWeatherRespond: FiveDaysHourlyWeatherRespond?) {
        binding.shimmer.stopShimmer()
        binding.shimmer.visibility = View.INVISIBLE
        adapter!!.setData(fiveDaysHourlyWeatherRespond!!.list)

        binding.cityName.text = fiveDaysHourlyWeatherRespond.city.name

        fiveDaysHourlyWeatherRespond.list[0].weather[0].main

        if (fiveDaysHourlyWeatherRespond.list[0].weather[0].main == "Thunderstorm") {
            binding.weatherMainImage.setImageResource(R.drawable.thunderstone)
        } else if (fiveDaysHourlyWeatherRespond.list[0].weather[0].main == "Drizzle") {
            binding.weatherMainImage.setImageResource(R.drawable.rain1)
        } else if (fiveDaysHourlyWeatherRespond.list[0].weather[0].main == "Rain") {
            binding.weatherMainImage.setImageResource(R.drawable.rain2)
        } else if (fiveDaysHourlyWeatherRespond.list[0].weather[0].main == "Snow") {
            binding.weatherMainImage.setImageResource(R.drawable.snow)
        } else if (fiveDaysHourlyWeatherRespond.list[0].weather[0].id in 701..798) {
            binding.weatherMainImage.setImageResource(R.drawable.mist)
        } else if (fiveDaysHourlyWeatherRespond.list[0].weather[0].main == "Clear") {
            binding.weatherMainImage.setImageResource(R.drawable.clear_sky_background)
        } else if (fiveDaysHourlyWeatherRespond.list[0].weather[0].id == 801 || fiveDaysHourlyWeatherRespond.list.get(
                0
            ).weather[0].id == 802
        ) {
            binding.weatherMainImage.setImageResource(R.drawable.fewclouds)
        } else if (fiveDaysHourlyWeatherRespond.list[0].weather[0].id == 803 || fiveDaysHourlyWeatherRespond.list.get(
                0
            ).weather[0].id == 804
        ) {
            binding.weatherMainImage.setImageResource(R.drawable.fewclouds)
        }

    }

    override fun onError(message: String) {
        Snackbar.make(requireContext(), binding.root, message, Snackbar.LENGTH_LONG).show()
        Timber.tag("TAG").d(message)
    }


}