package com.tetralogex.secureit.core.utils

import android.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.core.content.edit
import com.tetralogex.secureit.App
import com.tetralogex.secureit.core.AppConstants.IS_FIRST_TIME

object PrefUtils {
    private val sharedPreferences = getDefaultSharedPreferences(App.getContext())

    var isFirstTime
        get() = sharedPreferences.getBoolean(
            IS_FIRST_TIME,
            false
        )
        set(value) = sharedPreferences.edit { putBoolean(IS_FIRST_TIME, value) }
}