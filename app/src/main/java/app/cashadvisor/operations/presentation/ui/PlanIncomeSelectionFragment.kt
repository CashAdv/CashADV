package app.cashadvisor.operations.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.cashadvisor.R
import app.cashadvisor.databinding.FragmentPlanIncomeSelectionBinding

class PlanIncomeSelectionFragment : Fragment() {

    private var _binding: FragmentPlanIncomeSelectionBinding? = null
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
        _binding = FragmentPlanIncomeSelectionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bntSelectCategory.setOnClickListener {
            findNavController().navigate(R.id.action_planIncomeSelectionFragment_to_planIncomeFragment)
        }

        binding.bntAddCategory.setOnClickListener {
            findNavController().navigate(R.id.action_planIncomeSelectionFragment_to_categoriesFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}