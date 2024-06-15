package app.cashadvisor.analytics.presentation

import java.util.Calendar
import java.util.Date

fun Date.getLastDayOfMonth() : Date {
    val current = Calendar.getInstance()
    val lastDay = Calendar.getInstance()
    lastDay.set(current.get(Calendar.YEAR), current.get(Calendar.MONTH),
        current.getActualMaximum(Calendar.DATE))
    return lastDay.time
}

fun Date.getFirstDayOfMonth() : Date {
    val current = Calendar.getInstance()
    val firstDay = Calendar.getInstance()
    firstDay.set(current.get(Calendar.YEAR), current.get(Calendar.MONTH), 1)
    return firstDay.time
}