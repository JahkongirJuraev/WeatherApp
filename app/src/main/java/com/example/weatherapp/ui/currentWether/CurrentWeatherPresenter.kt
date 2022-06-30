package com.example.weatherapp.ui.currentWether

import com.example.weatherapp.core.cache.Cache
import com.example.weatherapp.core.constant.CONSTANTS
import com.example.weatherapp.core.model.WeatherData
import com.example.weatherapp.core.network.weatherNetwork.ApiClientModule
import com.example.weatherapp.core.network.weatherNetwork.services.WeatherService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response

class CurrentWeatherPresenter(val view: CurrentWeatherMVP.View) : CurrentWeatherMVP.Presenter {

    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    var currentWeatherService: WeatherService = ApiClientModule.getCurrentWeatherService()

    override fun loadCurrentWeather() {
        var disposable = currentWeatherService.getCurrentWeather(
            "metric", Cache.getInstance().cityName,
            CONSTANTS.apiKey
        ).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribeWith(
            object : DisposableSingleObserver<Response<WeatherData?>>() {
                override fun onSuccess(t: Response<WeatherData?>) {
                    if (t.isSuccessful) {
                        t.body()?.let {
                            view.getCurrentWeather(it)
                        }
                    } else {
                        view.onError(t.message().toString())
                    }
                }

                override fun onError(e: Throwable) {
                    view.onError(e.message.toString())
                }

            })
        compositeDisposable.add(disposable)
    }

    override fun cancelRequest() {
        compositeDisposable.dispose()
    }
}