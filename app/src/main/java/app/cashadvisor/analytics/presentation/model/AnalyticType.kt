package app.cashadvisor.analytics.presentation.model

import androidx.annotation.ColorRes
import app.cashadvisor.uikit.R

enum class AnalyticType(val value: Int) {
    INCOME(0),
    EXPENSE(1),
    SAVING(2);

    companion object {
        fun of(value: Int): AnalyticType {
            return when (value) {
                0 -> INCOME
                1 -> EXPENSE
                else -> SAVING
            }
        }
    }
}

@ColorRes
fun AnalyticType.getCategoryCurrencyTextColor(): Int {
    return when (this) {
        AnalyticType.INCOME -> R.color.m1
        AnalyticType.EXPENSE -> R.color.m2
        else -> R.color.m3
    }
}