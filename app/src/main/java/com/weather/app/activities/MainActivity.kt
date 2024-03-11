package com.weather.app.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.weather.app.R
import com.weather.app.adapters.WeatherListAdapter
import com.weather.app.models.ForecastModel
import com.weather.app.models.WeatherModel
import com.weather.app.viewModels.MainViewModel


class MainActivity : BaseActivity() {

    private lateinit var homeSwiperfreshlayout: SwipeRefreshLayout
    private lateinit var weatherDataRecyclerView: RecyclerView
    private lateinit var txtDegrees: TextView
    private lateinit var txtWeather: TextView
    private lateinit var headerContainer: LinearLayout

    private lateinit var weatherListAdapter: WeatherListAdapter
    private lateinit var mainViewModel: MainViewModel

    private val REQUEST_PERMISSIONS = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        initView()
        runApi()
    }

    fun initView() {
        homeSwiperfreshlayout = findViewById(R.id.homeSwiperfreshlayout)
        weatherDataRecyclerView = findViewById(R.id.weatherDataRecyclerView)
        txtDegrees = findViewById(R.id.txtDegrees)
        txtWeather = findViewById(R.id.txtWeather)
        headerContainer = findViewById(R.id.headerContainer)

        homeSwiperfreshlayout.setOnRefreshListener {
            runApi()
        }

        mainViewModel = MainViewModel(this)

        mainViewModel.mutableWeatherModel.observe(this) {
            updateView(weatherModel = it)
        }
        mainViewModel.mutableForecastModel.observe(this) {
            updateView(forecastModel = it)
        }
        mainViewModel.errorMessage.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
        mainViewModel.loading.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })
    }

    private fun runApi() {
        if (allPermissionsGranted())
            requestData()
        else
            requestPermissions()
    }

    private fun updateView(weatherModel: WeatherModel? = null, forecastModel: ForecastModel? = null) {
        if (weatherModel != null) {
            val degrees = weatherModel.weather.firstOrNull()?.id.toString()
            val description = weatherModel.weather.firstOrNull()?.description

            txtDegrees.text = degrees
            txtWeather.text = description

            weatherListAdapter = WeatherListAdapter(this, weatherModel.weather)
            weatherDataRecyclerView.adapter = weatherListAdapter
            weatherListAdapter.notifyDataSetChanged()

            val imageResourceId = when {
                description?.contains("rain") == true -> R.drawable.rain
                description?.contains("sun") == true -> R.drawable.forest_sunny
                description?.contains("cloud") == true -> R.drawable.forest_cloudy
                else -> R.drawable.forest_sunny
            }
            val headerContainerBackgroundDrawable = getResources().getDrawable(imageResourceId)
            headerContainer.background = headerContainerBackgroundDrawable
        }

        if (forecastModel != null) {
            val degrees = forecastModel.list.firstOrNull()?.weather?.firstOrNull()?.id.toString()
            val description = forecastModel.list.firstOrNull()?.weather?.firstOrNull()?.description.toString()

            txtDegrees.text = degrees
            txtWeather.text = description

            weatherListAdapter = WeatherListAdapter(this, forecastModel.list?.firstOrNull()?.weather)
            weatherDataRecyclerView.adapter = weatherListAdapter
            weatherListAdapter.notifyDataSetChanged()

            val imageResourceId = when {
                description?.contains("rain") == true -> R.drawable.rain
                description?.contains("sun") == true -> R.drawable.forest_sunny
                description?.contains("cloud") == true -> R.drawable.forest_cloudy
                else -> R.drawable.forest_sunny
            }
            val headerContainerBackgroundDrawable = getResources().getDrawable(imageResourceId)
            headerContainer.background = headerContainerBackgroundDrawable
        }
    }

    private fun allPermissionsGranted(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        } else if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        } /*else if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        } else if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }*/

        return true
    }

    private fun requestPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        } else if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
            return
        } /*else if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            return
        } else if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return
        }*/
    }

    private fun requestData() {
        mainViewModel.setLocation()
        mainViewModel.getWeatherData()
        mainViewModel.getForecastData()
    }

    private fun requestPermissions(permission: String) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_PERMISSIONS)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
            showToast(getString(R.string.permission_denied))
        else
            requestData()
    }
}