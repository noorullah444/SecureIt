package com.tetralogex.secureit.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.tetralogex.secureit.R
import com.tetralogex.secureit.core.extensions.click
import com.tetralogex.secureit.core.utils.PrefUtils.isNightMode
import com.tetralogex.secureit.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var binding: FragmentSettingsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        setupThemeRadioGroup()
    }

    private fun setupThemeRadioGroup() {
        binding?.radioGroup?.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_dark_mode -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    isNightMode = true
                }
                R.id.radio_light_mode -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    isNightMode = false
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }
    }

    override fun onStart() {
        setInitialTheme()
        super.onStart()
    }

    private fun setupClickListeners() {
        binding?.tvToneValue?.click {

        }

        binding?.tvPinCodeValue?.click {
            val action = SettingsFragmentDirections.actionSettingsFragmentToPinCodeFragment()
            findNavController().navigate(action)
        }
    }

    private fun setInitialTheme() {
        if (isNightMode) {
            binding?.radioDarkMode?.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            binding?.radioLightMode?.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}