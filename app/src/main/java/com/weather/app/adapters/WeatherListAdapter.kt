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
import com.weather.app.models.ForecastModel
import java.text.SimpleDateFormat

class WeatherListAdapter(
    var appCompatActivity: AppCompatActivity,
    var forecastModel: ForecastModel?
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

        var weatherItem = forecastModel?.list?.get(position)
        txtDay.text = getDayString(weatherItem?.dt_txt)
        txtDegrees.text = weatherItem?.main?.temp?.toString()
        imgListItem.background = getIconByWeatherText(weatherItem?.weather?.firstOrNull()?.description)
    }

    override fun getItemCount(): Int {
        return forecastModel?.list?.size ?: 0
    }

    private fun getDayString(dt_txt: String?): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val date = sdf.parse(dt_txt)
        return SimpleDateFormat("EEEE").format(date)
    }
    private fun getIconByWeatherText(description: String?): Drawable? {
        val resourceId = when {
            description?.toLowerCase()?.contains("rain") == true -> R.drawable.rain
            description?.toLowerCase()?.contains("sunny") == true -> R.drawable.partlysunny
            description?.toLowerCase()?.contains("clouds") == true -> R.drawable.clear
            else -> R.drawable.partlysunny
        }
        val headerContainerBackgroundDrawable = appCompatActivity.resources.getDrawable(resourceId)
        return headerContainerBackgroundDrawable
    }
}