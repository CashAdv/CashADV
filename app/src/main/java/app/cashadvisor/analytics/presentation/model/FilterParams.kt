package app.cashadvisor.analytics.presentation.model

import app.cashadvisor.analytics.presentation.getFirstDayOfMonth
import app.cashadvisor.analytics.presentation.getLastDayOfMonth
import java.util.Date

data class FilterParams(
    val beginDate: Date,
    val endDate: Date,
    val analyticType: AnalyticType,
    val planned: Boolean
) {

    companion object {
        fun getDefault(): FilterParams {
            return FilterParams(
                beginDate = Date().getFirstDayOfMonth(),
                endDate = Date().getLastDayOfMonth(),
                analyticType = AnalyticType.INCOME,
                planned = false)
        }
    }
}
