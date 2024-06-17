package app.cashadvisor.analytics.presentation.ui.state

import app.cashadvisor.analytics.presentation.model.Account
import app.cashadvisor.analytics.presentation.model.CategorySummary
import app.cashadvisor.analytics.presentation.model.FilterParams
import app.cashadvisor.analytics.presentation.model.User
import java.math.BigDecimal

sealed interface AnalyticsUiState {
    data class Content(
        val user: User?,
        val account: Account,
        val filterParams: FilterParams,
        val data: List<CategorySummary>?,
        ): AnalyticsUiState {
            fun getTotalAmountByFilter() : BigDecimal {
                var result = BigDecimal.valueOf(0)
                data?.let { data ->
                    data.stream()
                        .filter { categorySummary -> categorySummary.analyticType == filterParams.analyticType }
                        .filter { categorySummary -> categorySummary.planned == filterParams.planned}
                        .forEach { categorySummary -> result = result.add(categorySummary.amount) }
                }
                return result
            }

        fun getCategoryProgress(asPercentage: Boolean): BigDecimal {
            var progress = BigDecimal.valueOf(0)
            if (!filterParams.planned) {
                return progress
            }

            var sumPlan = BigDecimal.valueOf(0)
            data?.let { data ->
                data.stream()
                    .filter { categorySummary -> categorySummary.analyticType == filterParams.analyticType }
                    .filter { categorySummary -> categorySummary.planned }
                    .forEach { categorySummary -> sumPlan = sumPlan.add(categorySummary.amount) }
            }
            if (sumPlan.compareTo(BigDecimal.ZERO) == 0) {
                return progress
            }

            var sumFact = BigDecimal.valueOf(0)
            data?.let { data ->
                data.stream()
                    .filter { categorySummary -> categorySummary.analyticType == filterParams.analyticType }
                    .filter { categorySummary -> !categorySummary.planned }
                    .forEach { categorySummary -> sumFact = sumFact.add(categorySummary.amount) }
            }

            progress = if (asPercentage) {
                sumFact.divide(sumPlan).movePointRight(2)
            } else {
                sumPlan.minus(sumFact)
            }

            return progress
        }

            companion object {
                fun getDefault(): Content {
                    return Content(
                        user = null,
                        account = Account(amount = BigDecimal.ZERO),
                        filterParams = FilterParams.getDefault(),
                        data = null
                    )
                }
            }
        }
}