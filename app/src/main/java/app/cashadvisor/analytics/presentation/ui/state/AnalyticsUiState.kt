package app.cashadvisor.analytics.presentation.ui.state

import app.cashadvisor.analytics.presentation.model.Account
import app.cashadvisor.analytics.presentation.model.CategorySummary
import app.cashadvisor.analytics.presentation.model.FilterParams
import app.cashadvisor.analytics.presentation.model.User

sealed interface AnalyticsUiState {
    data class Content(
        val user: User,
        val account: Account,
        val filterParams: FilterParams,
        val data: List<CategorySummary>,
        ): AnalyticsUiState
}