package me.tino.fakeoutlook.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * global executor for the application
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 22, 18:58.
 */
data class AppExecutors(
    val background: Executor,
    val network: Executor,
    val main: Executor
) {

    class MainExecutor : Executor {
        private val mainHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainHandler.post(command)
        }
    }
}

/**
 * singleton for executor holder
 */
object OutlookExecutos {
    val executors by lazy {
        AppExecutors(
            Executors.newSingleThreadExecutor(),
            Executors.newFixedThreadPool(4),
            AppExecutors.MainExecutor()
        )
    }
}

