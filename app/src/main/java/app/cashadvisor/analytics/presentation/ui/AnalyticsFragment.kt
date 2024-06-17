package app.cashadvisor.analytics.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import app.cashadvisor.R
import app.cashadvisor.analytics.presentation.formatAmount
import app.cashadvisor.analytics.presentation.model.AnalyticType
import app.cashadvisor.analytics.presentation.model.FilterParams
import app.cashadvisor.analytics.presentation.ui.state.AnalyticsUiState
import app.cashadvisor.common.ui.BaseFragment
import app.cashadvisor.databinding.FragmentAnalyticsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Date

class AnalyticsFragment : BaseFragment<FragmentAnalyticsBinding, AnalyticsViewModel>(FragmentAnalyticsBinding::inflate) {

    override val viewModel: AnalyticsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileHeader.setOnClickListener {
            findNavController().navigate(R.id.action_analyticsFragment_to_profileSettingsFragment)
        }
    }

    override fun onConfigureViews() {
        with (binding) {
            cgAnalyticType.setOnCheckedStateChangeListener { chipGroup, _ ->
                Log.e("rrr", "chipGroup.checkedChipId = " + chipGroup.checkedChipId)
                setAnalyticType(chipGroup.checkedChipId)
            }
            cgPlanned.setOnCheckedStateChangeListener { chipGroup, _ ->
                setPlanned(chipGroup.checkedChipId)
            }
            // добавить выбор периода setPeriod
        }
    }

    override fun onSubscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.analyticsUiState.collectLatest { state ->
                    renderState(state)
                }
            }
        }
    }

    private fun renderState(state: AnalyticsUiState) {
        val content = state as AnalyticsUiState.Content
        setTotalAnalyticAmount(content.getTotalAmountByFilter())
        tuneCategoryProgressBar(state)
        tuneNavigationForState(content.filterParams)
    }

    private fun tuneCategoryProgressBar(state: AnalyticsUiState.Content) {
        binding.grProgress.isVisible = false
        if (state.filterParams.planned) {
            with (binding) {
                tvProgressInfoLabel.text = getProgressInfoLabelText(state.filterParams.analyticType)
                tvProgressPercent.text = String.format("%s %%", state.getCategoryProgress(true).formatAmount())
                tvProgressInfoAmount.text = state.getCategoryProgress(false).formatAmount()
                grProgress.isVisible = true
            }

        }
    }

    private fun getProgressInfoLabelText(type: AnalyticType): String {
        return when (type) {
            AnalyticType.INCOME -> getString(app.cashadvisor.uikit.R.string.mp_progress_info_income_label)
            AnalyticType.EXPENSE -> getString(app.cashadvisor.uikit.R.string.mp_progress_info_expense_label)
            else -> getString(app.cashadvisor.uikit.R.string.mp_progress_info_saving_label)
        }
    }

    private fun setTotalAnalyticAmount(amount: BigDecimal) {
        binding.tvAnalyticAmount.text = amount.formatAmount()
    }

    private fun tuneNavigationForState(params: FilterParams) {
        with (binding) {
            btnAdd.text = getAddButtonText(params.analyticType, params.planned)
            btnAdd.setOnClickListener {
                findNavController().navigate(getAddButtonNavigationId(params.analyticType, params.planned))
            }
            btnExAdd.isVisible = !params.planned
            if (btnExAdd.isVisible) {
                btnExAdd.setOnClickListener {
                    findNavController().navigate(getAddExButtonNavigationId(params.analyticType))
                }
            }
        }
    }

    private fun getAddButtonText(type: AnalyticType, planned: Boolean): String {
        return if (!planned) getString(app.cashadvisor.uikit.R.string.mp_add_bank_button) else {
            when (type) {
                AnalyticType.INCOME -> getString(app.cashadvisor.uikit.R.string.mp_add_income_button)
                AnalyticType.EXPENSE -> getString(app.cashadvisor.uikit.R.string.mp_add_expense_button)
                else -> getString(app.cashadvisor.uikit.R.string.mp_add_saving_button)
            }
        }
    }

    @IdRes
    private fun getAddButtonNavigationId(type: AnalyticType, planned: Boolean): Int {
        return if (planned) {
            when (type) {
                AnalyticType.INCOME -> R.id.action_analyticsFragment_to_planIncomeSelectionFragment
                AnalyticType.EXPENSE -> R.id.action_analyticsFragment_to_planExpenseSelectionFragment
                else -> R.id.action_analyticsFragment_to_planSavingSelectionFragment
            }
        } else {
            R.id.action_analyticsFragment_to_addBankSelectionFragment
        }

    }

    @IdRes
    private fun getAddExButtonNavigationId(type: AnalyticType): Int {
        return when (type) {
            AnalyticType.INCOME -> R.id.action_analyticsFragment_to_addIncomeSelectionFragment
            AnalyticType.EXPENSE -> R.id.action_analyticsFragment_to_addExpenseSelectionFragment
            else -> R.id.action_analyticsFragment_to_addSavingSelectionFragment
        }
    }

    private fun setAnalyticType(@IdRes checkedChipId: Int) {
        val type = when (checkedChipId) {
            binding.btnIncome.id -> AnalyticType.INCOME
            binding.btnExpense.id -> AnalyticType.EXPENSE
            binding.btnSaving.id -> AnalyticType.SAVING
            else -> AnalyticType.INCOME
        }
        viewModel.setAnalyticType(type)
    }

    private fun setPlanned(@IdRes checkedChipId: Int) {
        val planned = checkedChipId == binding.btnPlanFilter.id
        viewModel.setPlanned(planned)
    }

    private fun setPeriod(beginDate: Date, endDate: Date) {
        viewModel.setPeriod(beginDate, endDate)
    }
}