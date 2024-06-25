package app.cashadvisor.common.utils

import java.math.BigDecimal
import java.text.NumberFormat

class MoneyFormatter {
    private val formatter: NumberFormat = NumberFormat.getNumberInstance()

    fun format(amount: BigDecimal): String {
        formatter.maximumFractionDigits = MAX_FRACTION_DIGITS
        formatter.minimumFractionDigits = MIN_FRACTION_DIGITS
        return formatter.format(amount)
    }

    companion object {
        private const val MIN_FRACTION_DIGITS = 2
        private const val MAX_FRACTION_DIGITS = 2
    }
}