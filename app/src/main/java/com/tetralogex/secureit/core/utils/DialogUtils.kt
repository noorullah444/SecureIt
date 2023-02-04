package com.tetralogex.secureit.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.tetralogex.secureit.R
import com.tetralogex.secureit.core.AppConstants
import timber.log.Timber

object DialogUtils {

    fun isGPSEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun enableLocationSettings(activity: Activity, requestCode: Int) {
        Timber.d("enableLocationSettings: called")
        val locationRequest1 = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(500)
            .setMaxUpdateDelayMillis(100)
            .build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest1)
        LocationServices.getSettingsClient(activity)
            .checkLocationSettings(builder.build())
            .addOnSuccessListener(
                activity
            ) { response: LocationSettingsResponse? ->
                Timber.d("enableLocationSettings: $response")
            }
            .addOnFailureListener(
                activity
            ) { ex: java.lang.Exception? ->
                Timber.d("enableLocationSettings: exception=  ${ex?.message}")
                if (ex is ResolvableApiException) {
                    // Location settings are NOT satisfied,  but this can be fixed  by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),  and check the result in onActivityResult().
                        ex.startResolutionForResult(
                            activity,
                            requestCode
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                        Timber.d("enableLocationSettings: ${sendEx.message}")
                    }
                }
            }
    }

    fun showPermissionSettingsDialog(context: Context) {
        AlertDialog.Builder(context, R.style.DialogTheme)
            .setTitle(R.string.allow_permission_text)
            .setMessage(R.string.permanently_denied_message)
            .setNegativeButton(
                context.resources.getString(R.string.cancel)
            ) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(
                context.resources.getString(R.string.settings)
            ) { _, _ ->
                val intent = Intent()
                intent.action =
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts(
                    "package",
                    context.packageName,
                    null
                )
                intent.data = uri
                context.startActivity(intent)
            }
            .show()
    }
}