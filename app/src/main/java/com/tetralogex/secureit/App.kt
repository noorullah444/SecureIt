package com.tetralogex.secureit

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.tetralogex.secureit.core.utils.PrefUtils.isNightMode
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        setInitialTheme()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        private var instance: App? = null

        fun getContext(): App {
            return instance!!
        }

    }

    private fun setInitialTheme() {
        if (isNightMode) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}