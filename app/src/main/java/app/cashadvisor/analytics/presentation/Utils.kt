package app.cashadvisor.analytics.presentation

import java.math.BigDecimal
import java.text.NumberFormat

fun BigDecimal.formatAmount() : String {
    val formatter = NumberFormat.getNumberInstance()
    formatter.minimumFractionDigits = 2
    formatter.maximumFractionDigits = 2
    return formatter.format(this)
}