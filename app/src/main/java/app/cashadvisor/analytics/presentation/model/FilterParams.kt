package app.cashadvisor.analytics.presentation.model

import java.util.Calendar
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
                beginDate = Calendar.getInstance().time,
                endDate = Calendar.getInstance().time,
                analyticType = AnalyticType.INCOME,
                planned = false)
        }
    }
}
