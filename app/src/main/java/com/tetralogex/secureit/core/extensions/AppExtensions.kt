package com.tetralogex.secureit.core.extensions

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes

fun View.show() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun Context.toast(message: String ) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(@StringRes stringRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    showToast(getString(stringRes), duration)
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

infix fun View.click(onClick: () -> Unit) {
    setOnClickListener { onClick() }
}