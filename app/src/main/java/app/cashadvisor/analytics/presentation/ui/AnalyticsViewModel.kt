package app.cashadvisor.analytics.presentation.ui

import androidx.lifecycle.viewModelScope
import app.cashadvisor.analytics.presentation.MockData
import app.cashadvisor.analytics.presentation.getFirstDayOfMonth
import app.cashadvisor.analytics.presentation.getLastDayOfMonth
import app.cashadvisor.analytics.presentation.model.Account
import app.cashadvisor.analytics.presentation.model.AnalyticType
import app.cashadvisor.analytics.presentation.model.CategorySummary
import app.cashadvisor.analytics.presentation.model.FilterParams
import app.cashadvisor.analytics.presentation.ui.state.AnalyticsUiState
import app.cashadvisor.common.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor() : BaseViewModel() {
    private val _analyticsUiState = MutableStateFlow<AnalyticsUiState>(AnalyticsUiState.Default)
    val analyticsUiState: StateFlow<AnalyticsUiState>
        get() = _analyticsUiState
    private val _filterParams = MutableStateFlow(FilterParams.getDefault())
    private val filterParams: StateFlow<FilterParams>
        get() = _filterParams
    private val _categorySummaryList = MutableStateFlow<List<CategorySummary>>(emptyList())
    private val categorySummaryList: StateFlow<List<CategorySummary>>
        get() = _categorySummaryList

    init {
        setPeriod(Date().getFirstDayOfMonth(), Date().getLastDayOfMonth())
        observeFilterParams()
        observeCategorySummaryList()
    }

    private fun observeFilterParams() {
        viewModelScope.launch {
            filterParams.collectLatest{ params -> applyFilter(params) }
        }
    }

    private fun observeCategorySummaryList() {
        viewModelScope.launch {
            _categorySummaryList.collectLatest{ list -> setContent(list) }
        }
    }

    private fun applyFilter(params: FilterParams) {
        when (_analyticsUiState.value) {
            is AnalyticsUiState.Default -> loadData(params)
            is AnalyticsUiState.Loading -> loadData(params)
            is AnalyticsUiState.Content -> {
                val state = _analyticsUiState.value as AnalyticsUiState.Content
                if (params.beginDate == state.filterParams.beginDate &&
                    params.endDate == state.filterParams.endDate) {
                    _analyticsUiState.value = state.copy(
                        filterParams = params.copy(),
                        data = _categorySummaryList.value.filter {
                                categorySummary ->  categorySummary.analyticType == params.analyticType &&
                                categorySummary.planned == params.planned
                        },
                        completePercent = getCategoryProgress(true),
                        remainAmount = getCategoryProgress(false)
                    )
                } else {
                    loadData(params)
                }
            }
        }
    }

    private fun loadData(params: FilterParams) {
        fetchCategorySummaryList(params.beginDate, params.endDate)
    }

    private fun fetchCategorySummaryList(beginDate: Date, endDate: Date) {
        viewModelScope.launch {
            _analyticsUiState.value = AnalyticsUiState.Loading(
                user = null,
                account = Account(BigDecimal.ZERO),
                filterParams = filterParams.value.copy()
            )
            delay(3000L)
            // заменить на получение данных из репозитория
            _categorySummaryList.value = MockData.getCategorySummaryList(beginDate, endDate)
        }
    }

    private fun setContent(list: List<CategorySummary>) {
        if (_analyticsUiState.value is AnalyticsUiState.Default) {
            return
        }
        _analyticsUiState.value = AnalyticsUiState.Content(
            user = null,
            account = Account(BigDecimal.ZERO),
            filterParams = filterParams.value.copy(),
            data = list.filter { categorySummary ->
                categorySummary.analyticType == filterParams.value.analyticType &&
                        categorySummary.planned == filterParams.value.planned
            },
            completePercent = getCategoryProgress(true),
            remainAmount = getCategoryProgress(false)
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

    private fun getCategoryProgress(asPercentage: Boolean): BigDecimal {
        var progress = BigDecimal.valueOf(0)
        if (!filterParams.value.planned) {
            return progress
        }

        var sumPlan = BigDecimal.valueOf(0)
        categorySummaryList.value.let { data ->
            data
                .filter { categorySummary -> categorySummary.analyticType == filterParams.value.analyticType }
                .filter { categorySummary -> categorySummary.planned }
                .forEach { categorySummary ->
                    sumPlan = sumPlan.add(categorySummary.amount)
                }
        }
        if (sumPlan.compareTo(BigDecimal.ZERO) == 0) {
            return progress
        }

        var sumFact = BigDecimal.valueOf(0)
        categorySummaryList.value.let { data ->
            data
                .filter { categorySummary -> categorySummary.analyticType == filterParams.value.analyticType }
                .filter { categorySummary -> !categorySummary.planned }
                .forEach { categorySummary ->
                    sumFact = sumFact.add(categorySummary.amount)
                }
        }
        progress = if (asPercentage) {
            sumFact.divide(sumPlan).movePointRight(2)
        } else {
            sumPlan.minus(sumFact)
        }

        return progress
    }
}