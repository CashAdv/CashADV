package app.cashadvisor.settings.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.cashadvisor.R
import app.cashadvisor.databinding.FragmentFeaturesAndSettingsBinding

class FeaturesAndSettingsFragment : Fragment() {

    private var _binding: FragmentFeaturesAndSettingsBinding? = null
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
        _binding = FragmentFeaturesAndSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAccounts.setOnClickListener {
            findNavController().navigate(R.id.action_featuresAndSettingsFragment_to_bankAccountsFragment)
        }

        binding.btnArchive.setOnClickListener {
            findNavController().navigate(R.id.action_featuresAndSettingsFragment_to_operationsArchiveFragment)
        }

        binding.btnCategories.setOnClickListener {
            findNavController().navigate(R.id.action_featuresAndSettingsFragment_to_categoriesFragment)
        }

        binding.btnReports.setOnClickListener {
            findNavController().navigate(R.id.action_featuresAndSettingsFragment_to_exportingReportsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}