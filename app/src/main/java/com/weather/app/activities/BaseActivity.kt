package com.weather.app.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.weather.app.R

open class BaseActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(this)
    }

    fun showProgressDialog() {
        /*progressDialog.setTitle(getString(R.string.app_name))
        progressDialog.setMessage(getString(R.string.loading))
        progressDialog.show()*/
    }

    fun hideProgressDialog() {
        /*if (progressDialog.isShowing)
            progressDialog?.hide()*/
    }

    fun showToast(text: String?) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}