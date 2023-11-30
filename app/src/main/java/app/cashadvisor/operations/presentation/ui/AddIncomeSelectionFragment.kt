package app.cashadvisor.operations.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.cashadvisor.R
import app.cashadvisor.databinding.FragmentAddIncomeSelectionBinding

class AddIncomeSelectionFragment : Fragment() {

    private var _binding: FragmentAddIncomeSelectionBinding? = null
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
        _binding = FragmentAddIncomeSelectionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bntSelectCategory.setOnClickListener {
            findNavController().navigate(R.id.action_addIncomeSelectionFragment_to_addIncomeFragment)
        }

        binding.bntAddCategory.setOnClickListener {
            findNavController().navigate(R.id.action_addIncomeSelectionFragment_to_categoriesFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}