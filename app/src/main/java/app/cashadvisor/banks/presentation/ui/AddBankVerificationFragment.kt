package app.cashadvisor.banks.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.cashadvisor.R
import app.cashadvisor.databinding.FragmentAddBankVerificationBinding

class AddBankVerificationFragment : Fragment() {

    private var _binding: FragmentAddBankVerificationBinding? = null
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
        _binding = FragmentAddBankVerificationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCodeInputAnalytics.setOnClickListener {
            findNavController().navigate(R.id.action_addBankVerificationFragment_to_analyticsFragment)
        }

        binding.btnCodeInputTracker.setOnClickListener {
            findNavController().navigate(R.id.action_addBankVerificationFragment_to_trackerFragment)
        }

        binding.btnCodeInputSettings.setOnClickListener {
            findNavController().navigate(R.id.action_addBankVerificationFragment_to_bankAccountsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}