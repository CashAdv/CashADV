package app.cashadvisor.analytics.data.db.entities.converteters

import java.text.SimpleDateFormat
import java.util.Date

class DateConverter {

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM.yyyy")
        return format.format(date)
    }

    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("dd.MM.yyyy")
        return df.parse(date)?.time ?: 0
    }

}