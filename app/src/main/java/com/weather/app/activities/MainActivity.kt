package com.weather.app.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.weather.app.R
import com.weather.app.adapters.WeatherListAdapter
import com.weather.app.models.ForecastModel
import com.weather.app.models.Main
import com.weather.app.models.Weather
import com.weather.app.models.WeatherModel
import com.weather.app.viewModels.MainViewModel

class MainActivity : BaseActivity() {

    private lateinit var loadingOverlay: RelativeLayout
    private lateinit var homeSwiperfreshlayout: SwipeRefreshLayout
    private lateinit var layoutBottomContainer: LinearLayout
    private lateinit var weatherDataRecyclerView: RecyclerView
    private lateinit var txtMin: TextView
    private lateinit var txtCurrent: TextView
    private lateinit var txtMax: TextView
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
        loadingOverlay = findViewById(R.id.loadingOverlay)
        homeSwiperfreshlayout = findViewById(R.id.homeSwiperfreshlayout)
        layoutBottomContainer = findViewById(R.id.layoutBottomContainer)
        weatherDataRecyclerView = findViewById(R.id.weatherDataRecyclerView)
        txtMin = findViewById(R.id.txtMin)
        txtCurrent = findViewById(R.id.txtCurrent)
        txtMax = findViewById(R.id.txtMax)
        txtDegrees = findViewById(R.id.txtDegrees)
        txtWeather = findViewById(R.id.txtWeather)
        headerContainer = findViewById(R.id.headerContainer)

        homeSwiperfreshlayout.setOnRefreshListener {
            runApi()
        }
        weatherListAdapter = WeatherListAdapter(this, null)

        mainViewModel = MainViewModel(this)
        mainViewModel.mutableWeatherModel.observe(this) {
            updateViewForWeather(it)
        }
        mainViewModel.mutableForecastModel.observe(this) {
            updateViewForForecast(it)
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

    private fun updateViewForWeather(
        weatherModel: WeatherModel?
    ) {
        homeSwiperfreshlayout.isRefreshing = false

        val main = weatherModel?.main
        val degrees = main?.temp?.toString()
        val description = weatherModel?.weather?.firstOrNull()?.description

        txtDegrees.text = degrees
        txtWeather.text = description

        headerContainer.background =
            getResources().getDrawable(getHeaderBackgroundByWeatherText(description))
        layoutBottomContainer.setBackgroundColor(getColor(getListBackgroundColorByWeatherText(description)))
    }

    private fun updateViewForForecast(
        forecastModel: ForecastModel?
    ) {
        homeSwiperfreshlayout.isRefreshing = false
        hideProgressDialog()

        val main = forecastModel?.list?.firstOrNull()?.main
        txtMin.text = main?.temp_min.toString() + "\nmin"
        txtCurrent.text = main?.temp.toString() + "\ncurrent"
        txtMax.text = main?.temp_max.toString() + "\nmax"

        weatherListAdapter = WeatherListAdapter(this, forecastModel)
        weatherDataRecyclerView.adapter = weatherListAdapter
        weatherListAdapter.notifyDataSetChanged()
    }

    private fun showProgressDialog() {
        loadingOverlay.visibility = ViewGroup.VISIBLE
    }

    private fun hideProgressDialog() {
        loadingOverlay.visibility = ViewGroup.GONE
    }

    private fun getHeaderBackgroundByWeatherText(description: String?): Int {
        return when {
            description?.toLowerCase()?.contains("rain") == true -> R.drawable.rain
            description?.toLowerCase()?.contains("sunny") == true -> R.drawable.forest_sunny
            description?.toLowerCase()?.contains("clouds") == true -> R.drawable.forest_cloudy
            else -> R.drawable.forest_sunny
        }
    }

    private fun getListBackgroundColorByWeatherText(weatherText: String?): Int {
        return when {
            weatherText?.toLowerCase()?.contains("rain") == true -> R.color.rainy
            weatherText?.toLowerCase()?.contains("sun") == true -> R.color.sunny
            weatherText?.toLowerCase()?.contains("cloud") == true -> R.color.cloudy
            else -> R.drawable.forest_sunny
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