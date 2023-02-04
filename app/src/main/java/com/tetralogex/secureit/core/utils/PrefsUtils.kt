package com.tetralogex.secureit.core.utils

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.core.content.edit
import com.tetralogex.secureit.App
import com.tetralogex.secureit.core.AppConstants.IS_ALARM_ENABLE
import com.tetralogex.secureit.core.AppConstants.IS_EMAIL_LOCATION
import com.tetralogex.secureit.core.AppConstants.IS_EMAIL_SELFIE
import com.tetralogex.secureit.core.AppConstants.IS_FIRST_TIME
import com.tetralogex.secureit.core.AppConstants.NIGHT_MODE
import com.tetralogex.secureit.core.AppConstants.PIN_CODE_PREFS

object PrefUtils {

    private val sharedPreferences: SharedPreferences =
        App.getContext().getSharedPreferences("AppSettingPrefs", MODE_PRIVATE)

    var pinCode
        get() = sharedPreferences.getInt(
            PIN_CODE_PREFS,
            1111
        )
        set(value) = sharedPreferences.edit() { putInt(PIN_CODE_PREFS, value) }

    var isEmailLocation
        get() = sharedPreferences.getBoolean(
            IS_EMAIL_LOCATION,
            false
        )
        set(value) = sharedPreferences.edit { putBoolean(IS_EMAIL_LOCATION, value) }

    var isEmailSelfie
        get() = sharedPreferences.getBoolean(
            IS_EMAIL_SELFIE,
            false
        )
        set(value) = sharedPreferences.edit { putBoolean(IS_EMAIL_SELFIE, value) }

    var isAlarmEnable
        get() = sharedPreferences.getBoolean(
            IS_ALARM_ENABLE,
            false
        )
        set(value) = sharedPreferences.edit { putBoolean(IS_ALARM_ENABLE, value) }

    var isNightMode
        get() = sharedPreferences.getBoolean(
            NIGHT_MODE,
            false
        )
        set(value) = sharedPreferences.edit { putBoolean(NIGHT_MODE, value) }

    var isFirstTime
        get() = sharedPreferences.getBoolean(
            IS_FIRST_TIME,
            false
        )
        set(value) = sharedPreferences.edit { putBoolean(IS_FIRST_TIME, value) }
}