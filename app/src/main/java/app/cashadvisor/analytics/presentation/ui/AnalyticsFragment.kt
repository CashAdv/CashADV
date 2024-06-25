package app.cashadvisor.analytics.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.cashadvisor.R
import app.cashadvisor.analytics.presentation.model.AnalyticType
import app.cashadvisor.analytics.presentation.model.FilterParams
import app.cashadvisor.analytics.presentation.ui.adapter.AnalyticInfoAdapter
import app.cashadvisor.analytics.presentation.ui.state.AnalyticsUiState
import app.cashadvisor.common.ui.BaseFragment
import app.cashadvisor.common.utils.MoneyFormatter
import app.cashadvisor.databinding.FragmentAnalyticsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Date
import javax.inject.Inject


@AndroidEntryPoint
class AnalyticsFragment : BaseFragment<FragmentAnalyticsBinding, AnalyticsViewModel>(FragmentAnalyticsBinding::inflate) {

    override val viewModel: AnalyticsViewModel by viewModels()
    private val analyticInfoAdapter by lazy { AnalyticInfoAdapter(formatter) }
    @Inject
    lateinit var formatter: MoneyFormatter

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

        initRecyclerView()
    }

    override fun onConfigureViews() {
        with (binding) {
            cgAnalyticType.setOnCheckedStateChangeListener { chipGroup, _ ->
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

    private fun initRecyclerView() {
        with(binding.rvAnalyticInfo) {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = analyticInfoAdapter
        }
    }

    private fun renderState(state: AnalyticsUiState) {
        binding.ltProgressView.root.isVisible = state is AnalyticsUiState.Loading
        binding.svAnalyticInfo.isVisible = state is AnalyticsUiState.Content
        binding.tvAnalyticInfoHeader.isVisible = state is AnalyticsUiState.Content

        if (state is AnalyticsUiState.Content) {
            val content = state as AnalyticsUiState.Content
            setAnalyticInfoHeader(content.filterParams)
            tuneAnalyticInfoPlaceHolder(content.data.isNullOrEmpty(), content.filterParams)
            analyticInfoAdapter.submitList(content.data)
            setTotalAnalyticAmount(content.getTotalAmountByFilter())
            tuneCategoryProgressBar(content)
            tuneNavigationForState(content.filterParams)
        }
    }

    private fun setAnalyticInfoHeader(filterParams: FilterParams) {
        binding.tvAnalyticInfoHeader.text = if (!filterParams.planned) {
            getString(app.cashadvisor.uikit.R.string.mp_fact_analytic_info_header)
        } else {
            when (filterParams.analyticType) {
                AnalyticType.INCOME -> getString(app.cashadvisor.uikit.R.string.mp_plan_income_analytic_info_header)
                AnalyticType.EXPENSE -> getString(app.cashadvisor.uikit.R.string.mp_plan_expense_analytic_info_header)
                else -> getString(app.cashadvisor.uikit.R.string.mp_plan_saving_analytic_info_header)
            }
        }
    }

    private fun tuneAnalyticInfoPlaceHolder(isVisible: Boolean, filterParams: FilterParams) {
        binding.tvAnalyticInfoPlaceHolder.text = getAnalyticInfoPlaceHolderText(
            filterParams.analyticType,
            filterParams.planned
        )
        binding.tvAnalyticInfoPlaceHolder.isVisible = isVisible
    }

    private fun getAnalyticInfoPlaceHolderText(type: AnalyticType, planned: Boolean): String {
        return if (planned) {
            when (type) {
                AnalyticType.INCOME -> getString(app.cashadvisor.uikit.R.string.mp_plan_income_analytic_info_zero)
                AnalyticType.EXPENSE -> getString(app.cashadvisor.uikit.R.string.mp_plan_expense_analytic_info_zero)
                else -> getString(app.cashadvisor.uikit.R.string.mp_plan_saving_analytic_info_zero)
            }
        } else {
            when (type) {
                AnalyticType.INCOME -> getString(app.cashadvisor.uikit.R.string.mp_fact_income_analytic_info_zero)
                AnalyticType.EXPENSE -> getString(app.cashadvisor.uikit.R.string.mp_fact_expense_analytic_info_zero)
                else -> getString(app.cashadvisor.uikit.R.string.mp_fact_saving_analytic_info_zero)
            }
        }
    }

    private fun tuneCategoryProgressBar(state: AnalyticsUiState.Content) {
        binding.grProgress.isVisible = false
        if (state.filterParams.planned) {
            with (binding) {
                tvProgressInfoLabel.text = getProgressInfoLabelText(state.filterParams.analyticType)
                val percent = state.completePercent
                val defaultDrawable = state.getTotalAmountByFilter().compareTo(BigDecimal.ZERO) == 0
                piAnalyticProgress.progressDrawable = AppCompatResources.getDrawable(
                    requireContext(),
                    getProgressDrawable(defaultDrawable)
                )
                piAnalyticProgress.progress = percent.toInt()
                tvProgressPercent.text = String.format("%s %%", formatter.format(percent))
                tvProgressInfoAmount.text = formatter.format(state.remainAmount)
                grProgress.isVisible = true
            }

        }
    }

    @DrawableRes
    private fun getProgressDrawable(defaultDrawable: Boolean): Int {
        return if (defaultDrawable) {
            app.cashadvisor.uikit.R.drawable.gradient_linear_progress_bar
        } else {
            app.cashadvisor.uikit.R.drawable.linear_progress_bar
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
        binding.tvAnalyticAmount.text = formatter.format(amount)
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