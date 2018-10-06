package me.tino.fakeoutlook

import android.app.Application
import android.app.IntentService
import io.reactivex.plugins.RxJavaPlugins
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import kotlin.properties.Delegates

/**
 * the entrance of the application, do the initialize before any activity
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 22, 23:42.
 */
class App: Application() {

    private lateinit var _retrofit: Retrofit

    val retrofit: Retrofit
        get() = _retrofit

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        //when the error deliver to the error handler, just log it
        RxJavaPlugins.setErrorHandler {
            Timber.e(it)
        }

        val client = OkHttpClient.Builder().build()
        _retrofit = Retrofit.Builder().baseUrl("https://api.darksky.net/forecast/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        instance = this
    }

    companion object {
        var instance by Delegates.notNull<App>()
    }
}