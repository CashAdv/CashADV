package app.cashadvisor.analytics.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.cashadvisor.R
import app.cashadvisor.databinding.FragmentAnalyticsBinding

class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddBank.setOnClickListener {
            findNavController().navigate(R.id.action_analyticsFragment_to_addBankSelectionFragment)
        }


        val planIncome = R.id.action_analyticsFragment_to_planIncomeSelectionFragment

        val planExpense = R.id.action_analyticsFragment_to_planExpenseSelectionFragment

        val planSaving = R.id.action_analyticsFragment_to_planSavingSelectionFragment

        val addIncome = R.id.action_analyticsFragment_to_addIncomeSelectionFragment

        val addExpense = R.id.action_analyticsFragment_to_addExpenseSelectionFragment

        val addSaving = R.id.action_analyticsFragment_to_addSavingSelectionFragment


        binding.btnAddManually.setOnClickListener {
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
            }
        }




    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}