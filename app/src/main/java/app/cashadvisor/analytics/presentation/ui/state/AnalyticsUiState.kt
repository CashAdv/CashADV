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