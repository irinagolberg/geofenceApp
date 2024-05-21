package com.test.employeepresence.ui.hours

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.test.employeepresence.databinding.LayoutHoursItemBinding
import com.test.employeepresence.hours.domain.DayRecord
import com.test.employeepresence.utils.APP_LOGTAG
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HoursAdapter : RecyclerView.Adapter<HoursAdapter.ViewHolder>() {
    companion object {
        val TITLE_DATE_FORMAT = SimpleDateFormat("EEEE, dd.MM.yyyy", Locale.getDefault())
        val TIME_FORMAT = SimpleDateFormat("hh:mm", Locale.getDefault())
    }

    private val dayRecords: MutableList<DayRecord> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = LayoutHoursItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(dayRecords[position])
    }

    override fun getItemCount(): Int = dayRecords.size

    fun updateHours(newRecords: List<DayRecord>) {
        dayRecords.clear()
        dayRecords.addAll(newRecords)
        Log.d(APP_LOGTAG, "updateHours: $dayRecords")
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: LayoutHoursItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dayRecord: DayRecord) = binding.run {
            Log.d(APP_LOGTAG, "update record: $dayRecord")

            with (dayRecord) {
                hoursTitle.text = TITLE_DATE_FORMAT.format(entrance.format(DateTimeFormatter.ISO_LOCAL_DATE))
                timeStart.text = TIME_FORMAT.format(entrance.time)
                timeEnd.text = TIME_FORMAT.format(exit.time)
                duration.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ChronoUnit.HOURS.between(entrance.time, exit.time).toString()
                } else {
                    ""
                }
            }
        }
    }
}