package com.example.todonotes

import android.content.Context
import android.util.Patterns
import android.widget.Toast

class ValidateInput internal constructor(private val context: Context) {
    fun checkIfEmailIsValid(email: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(context, "Please Enter Your Email Id", Toast.LENGTH_SHORT).show()
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Please Enter a Valid Email Id", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    fun checkIfPasswordIsValid(password: String): Boolean {
        if (password.isEmpty()) {
            Toast.makeText(context, "Please Enter Your Password", Toast.LENGTH_SHORT).show()
            return false
        } else if (password.length < 6) {
            Toast.makeText(context, "Please Enter a Password of at least 6 digits", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

}