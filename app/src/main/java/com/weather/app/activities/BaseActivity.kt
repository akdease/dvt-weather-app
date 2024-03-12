package com.weather.app.activities

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    fun showToast(text: String?) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}