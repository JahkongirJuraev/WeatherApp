package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapp.databinding.WeeksWaetherFragmentBinding

class WeeksWeather : Fragment() {

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

}