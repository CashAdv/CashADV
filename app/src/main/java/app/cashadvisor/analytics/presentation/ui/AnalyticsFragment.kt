package app.cashadvisor.analytics.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.cashadvisor.R
import app.cashadvisor.analytics.presentation.model.AnalyticType
import app.cashadvisor.common.ui.BaseFragment
import app.cashadvisor.databinding.FragmentAnalyticsBinding
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

        binding.btnAddBank.setOnClickListener {
            findNavController().navigate(R.id.action_analyticsFragment_to_addBankSelectionFragment)
        }


        val planIncome = R.id.action_analyticsFragment_to_planIncomeSelectionFragment

        val planExpense = R.id.action_analyticsFragment_to_planExpenseSelectionFragment

        val planSaving = R.id.action_analyticsFragment_to_planSavingSelectionFragment

        val addIncome = R.id.action_analyticsFragment_to_addIncomeSelectionFragment

        val addExpense = R.id.action_analyticsFragment_to_addExpenseSelectionFragment

        val addSaving = R.id.action_analyticsFragment_to_addSavingSelectionFragment


        binding.btnAddManually.setOnClickListener {/*
            with(binding) {
                when {
                    rbIncome.isChecked && rbPlan.isChecked -> {
                        findNavController().navigate(planIncome)
                    }

                    rbIncome.isChecked && rbFact.isChecked -> {
                        findNavController().navigate(addIncome)
                    }

                    rbExpense.isChecked && rbPlan.isChecked -> {
                        findNavController().navigate(planExpense)
                    }

                    rbExpense.isChecked && rbFact.isChecked -> {
                        findNavController().navigate(addExpense)
                    }

                    rbSaving.isChecked && rbPlan.isChecked -> {
                        findNavController().navigate(planSaving)
                    }

                    rbSaving.isChecked && rbFact.isChecked -> {
                        findNavController().navigate(addSaving)
                    }

                }
            }*/
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