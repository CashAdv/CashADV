package app.cashadvisor.analytics.presentation.ui.state

import app.cashadvisor.analytics.presentation.model.Account
import app.cashadvisor.analytics.presentation.model.CategorySummary
import app.cashadvisor.analytics.presentation.model.FilterParams
import app.cashadvisor.analytics.presentation.model.User
import java.math.BigDecimal

sealed interface AnalyticsUiState {
    data object Default : AnalyticsUiState

    data class Loading(
        val user: User?,
        val account: Account,
        val filterParams: FilterParams
    ) : AnalyticsUiState

    data class Content(
        val user: User?,
        val account: Account,
        val filterParams: FilterParams,
        val data: List<CategorySummary>?,
        val completePercent: BigDecimal,
        val remainAmount: BigDecimal,
    ) : AnalyticsUiState {
        fun getTotalAmountByFilter(): BigDecimal {
            var result = BigDecimal.valueOf(0)
            data?.let { data ->
                data.forEach { categorySummary ->
                    result = result.add(categorySummary.amount)
                }
            }
            return result
        }
    }
}