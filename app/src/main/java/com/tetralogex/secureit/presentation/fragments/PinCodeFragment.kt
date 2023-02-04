package com.tetralogex.secureit.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tetralogex.secureit.core.extensions.click
import com.tetralogex.secureit.core.extensions.toast
import com.tetralogex.secureit.core.utils.PrefUtils.pinCode
import com.tetralogex.secureit.core.utils.TextValidator
import com.tetralogex.secureit.databinding.FragmentPinCodeBinding


class PinCodeFragment : Fragment() {
    private var binding: FragmentPinCodeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPinCodeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.editTextPin?.setText(pinCode.toString())
        binding?.editTextPin?.addTextChangedListener(object : TextValidator(binding!!.editTextPin) {
            override fun validate(textView: TextView?, text: String?) {
                /* Validation code here */
                if (text?.length!! != 4) {
                    binding?.textInputLayout?.error = "PIN must have 4 digits"
                    binding?.btnSave?.isClickable = false
                } else {
                    binding?.textInputLayout?.error = ""
                    binding?.btnSave?.isClickable = true
                }
            }
        })

        binding?.btnSave?.click {
            val pin = binding?.editTextPin?.text
            when {
                pin?.toString()?.equals(pinCode.toString()) == true -> {
                    requireContext().toast("PIN already saved!")
                }
                pin?.length!! != 4 -> {
                    requireContext().toast("PIN must have 4 digits")
                }
                else -> {
                    pinCode = pin.toString().toInt()
                    requireContext().toast("PIN code saved successfully!")
                }
            }
        }
    }
}