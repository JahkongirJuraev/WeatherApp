package com.example.weatherapp.ui.fiveDaysWeather

import com.example.weatherapp.core.cache.Cache
import com.example.weatherapp.core.constant.CONSTANTS
import com.example.weatherapp.core.model.FiveDaysWeatherResponse.FiveDaysHourlyWeatherRespond
import com.example.weatherapp.core.network.weatherNetwork.ApiClientModule
import com.example.weatherapp.core.network.weatherNetwork.services.FiveDaysWeatherService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response

class FiveDaysWeatherPresenter(val view: FiveDaysWeatherMVP.View) : FiveDaysWeatherMVP.Presenter {

    var service: FiveDaysWeatherService = ApiClientModule.getFiveDaysWeatherService()

    var compositeDisposable = CompositeDisposable()

    override fun loadFiveDaysWeather() {
        var disposable = service.getFiveDaysWeather(
            cityName = Cache.getInstance().cityName,
            apiKey = CONSTANTS.apiKey,
            units = "metric"
        ).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribeWith(
            object : DisposableSingleObserver<Response<FiveDaysHourlyWeatherRespond?>>() {
                override fun onSuccess(t: Response<FiveDaysHourlyWeatherRespond?>) {
                    if (t.isSuccessful) {
                        t.body()?.let {
                            view.getFiveDaysWeather(it)
                        }
                    } else {
                        // view.onError(t.message().toString())
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