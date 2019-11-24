package com.example.seriestracker

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.databinding.InverseBindingAdapter




// Code to hide keyboard
fun Fragment.hideKeyboard() { view?.let { activity?.hideKeyboard(it) } }
fun Context.hideKeyboard(view: View) { val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0) }

