package com.orangeink.utils

import android.util.Patterns
import java.util.regex.Pattern

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPhoneNumber() = Pattern.compile("^[6-9]\\d{9}\$").matcher(this).matches()