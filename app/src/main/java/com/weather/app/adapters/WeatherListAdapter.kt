package com.weather.app.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.weather.app.R
import com.weather.app.models.Weather

class WeatherListAdapter(
    val appCompatActivity: AppCompatActivity,
    val weatherList: List<Weather>?
) : RecyclerView.Adapter<WeatherListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_list_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val view = viewHolder.itemView
        val imgListItem: ImageView = view.findViewById(R.id.imgListItem)
        val txtDay: TextView = view.findViewById(R.id.txtDay)
        val txtDegrees: TextView = view.findViewById(R.id.txtDegrees)

        var weatherItem = weatherList?.firstOrNull()
        txtDay.text = weatherItem?.description
        txtDegrees.text = weatherItem?.description
        imgListItem.background = getIconByWeatherText(weatherItem?.description)
    }

    override fun getItemCount(): Int {
        return weatherList?.size ?: 0
    }

    private fun getIconByWeatherText(description: String?): Drawable? {
         val resourceId = when {
            description?.toLowerCase()?.contains("rain") == true -> R.drawable.rain
            description?.toLowerCase()?.contains("sunny") == true -> R.drawable.forest_sunny
            description?.toLowerCase()?.contains("clouds") == true -> R.drawable.forest_cloudy
            else -> R.drawable.forest_sunny
        }
        val headerContainerBackgroundDrawable = appCompatActivity.getDrawable(resourceId)
        return headerContainerBackgroundDrawable
    }
}