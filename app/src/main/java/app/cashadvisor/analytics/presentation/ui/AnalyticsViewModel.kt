package app.cashadvisor.analytics.presentation.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.cashadvisor.analytics.presentation.model.AnalyticType
import app.cashadvisor.analytics.presentation.model.FilterParams
import app.cashadvisor.analytics.presentation.ui.state.AnalyticsUiState
import app.cashadvisor.common.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor() : BaseViewModel() {
    private val _analyticsUiState = MutableStateFlow(AnalyticsUiState.Content.getDefault())
    val analyticsUiState: StateFlow<AnalyticsUiState>
        get() = _analyticsUiState
    private val _filterParams = MutableStateFlow(FilterParams.getDefault())
    private val filterParams: StateFlow<FilterParams>
        get() = _filterParams

    init {
        observeFilterParams()
    }

    private fun observeFilterParams() {
        viewModelScope.launch {
            filterParams.collectLatest{ params -> applyFilter(params) }
        }
    }

    private fun applyFilter(params: FilterParams) {
        Log.e("rrr", "params.beginDate = " + params.beginDate.toString())
        Log.e("rrr", "params.endDate = " + params.endDate.toString())
        Log.e("rrr", "params.analyticType = " + params.analyticType.toString())
        Log.e("rrr", "params.planned = " + params.planned.toString())
        _analyticsUiState.value = _analyticsUiState.value.copy(
            filterParams = params.copy()
        )
    }

    fun setAnalyticType(analyticType: AnalyticType) {
        _filterParams.value = _filterParams.value.copy(
            analyticType = analyticType
        )
    }

    fun setPlanned(planned: Boolean) {
        _filterParams.value = _filterParams.value.copy(
            planned = planned
        )
    }

    fun setPeriod(beginDate: Date, endDate: Date) {
        _filterParams.value = _filterParams.value.copy(
            beginDate = beginDate,
            endDate = endDate
        )
    }
}