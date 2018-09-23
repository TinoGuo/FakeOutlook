package me.tino.fakeoutlook

import android.app.Application
import timber.log.Timber

/**
 * the entrance of the application, do the initialize before any activity
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 22, 23:42.
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}