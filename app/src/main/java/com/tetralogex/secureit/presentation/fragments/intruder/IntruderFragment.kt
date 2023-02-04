package com.tetralogex.secureit.presentation.fragments.intruder

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.tetralogex.secureit.R
import com.tetralogex.secureit.core.AppConstants.REQUEST_CODE_CHECK_SETTINGS
import com.tetralogex.secureit.core.extensions.toast
import com.tetralogex.secureit.core.utils.DialogUtils.isGPSEnabled
import com.tetralogex.secureit.core.utils.DialogUtils.showPermissionSettingsDialog
import com.tetralogex.secureit.core.utils.PrefUtils.isAlarmEnable
import com.tetralogex.secureit.core.utils.PrefUtils.isEmailLocation
import com.tetralogex.secureit.core.utils.PrefUtils.isEmailSelfie
import com.tetralogex.secureit.databinding.FragmentIntruderBinding
import timber.log.Timber

class IntruderFragment : Fragment() {
    private var binding: FragmentIntruderBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIntruderBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        initViews()
    }

    override fun onResume() {
        super.onResume()
        binding?.checkBoxAlarm?.isChecked = isAlarmEnable
        binding?.checkBoxSelfie?.isChecked = isEmailSelfie
        binding?.checkBoxLocation?.isChecked = isEmailLocation
    }

    private fun initViews() {
        binding?.checkBoxAlarm?.setOnCheckedChangeListener { _, isChecked ->
            isAlarmEnable = isChecked
        }
        binding?.checkBoxSelfie?.setOnCheckedChangeListener { _, isChecked ->
            isEmailSelfie = isChecked
        }
        binding?.checkBoxLocation?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkLocationPermission()
            }
        }
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                if (!isGPSEnabled(requireContext())) {
                    enableLocationSettings(
                        requireActivity(),
                        REQUEST_CODE_CHECK_SETTINGS
                    )
                } else enableEmailLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
                showPermissionSettingsDialog(requireContext())
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    private fun disableEmailLocation() {
        isEmailLocation = false
        binding?.checkBoxLocation?.isChecked = false
    }

    private fun enableEmailLocation() {
        isEmailLocation = true
        binding?.checkBoxLocation?.isChecked = true
    }

    private

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
                        /*ex.startResolutionForResult(
                            activity,
                            requestCode
                        )*/

                        val intentSenderRequest = IntentSenderRequest.Builder(ex.resolution).build()
                        resolutionForResult.launch(intentSenderRequest)

                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                        Timber.d("enableLocationSettings: ${sendEx.message}")
                    }
                }
            }
    }

    private val resolutionForResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                //startLocationUpdates() or do whatever you want
                Timber.d("enableLocationSettings: gps enabled")
                enableEmailLocation()
            } else {
                Timber.d("enableLocationSettings: gps disabled")
                disableEmailLocation()
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Timber.d("permission granted")
                // Permission is granted. Continue the action or workflow in your app.
                if (!isGPSEnabled(requireContext())) {
                    enableLocationSettings(
                        requireActivity(),
                        REQUEST_CODE_CHECK_SETTINGS
                    )
                } else enableEmailLocation()
            } else {
                Timber.d("permission denied")
                disableEmailLocation()
                requireContext().toast("Permission denied!")
            }
        }

    private fun setupMenu() {
        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_intruders, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.settingsFragment -> {
                        navigateToSettings()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun navigateToSettings() {
        if (findNavController().currentDestination?.id == R.id.intruderFragment) {
            val action = IntruderFragmentDirections.actionIntruderFragmentToSettingsFragment()
            findNavController().navigate(action)
        }
    }
}