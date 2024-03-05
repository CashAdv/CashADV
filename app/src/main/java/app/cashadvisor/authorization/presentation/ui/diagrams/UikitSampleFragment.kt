package app.cashadvisor.authorization.presentation.ui.diagrams

import androidx.navigation.fragment.findNavController
import app.cashadvisor.R
import app.cashadvisor.common.ui.BaseFragment
import app.cashadvisor.common.ui.BaseViewModel
import app.cashadvisor.databinding.FragmentUikitSampleBinding

class UikitSampleViewModel : BaseViewModel() {
    // example viewModel
}

class UikitSampleFragment :
    BaseFragment<FragmentUikitSampleBinding, UikitSampleViewModel>(FragmentUikitSampleBinding::inflate) {
    override val viewModel: UikitSampleViewModel by lazy { UikitSampleViewModel() }


    override fun onConfigureViews() {

        binding.firstDiagramButton.setOnClickListener {
            findNavController().navigate(R.id.action_uikitSampleFragment_to_firstDiagramFragment)
        }
        binding.secondDiagramButton.setOnClickListener {
            findNavController().navigate(R.id.action_uikitSampleFragment_to_secondDiagramFragment)
        }
        binding.thirdDiagramButton.setOnClickListener {
            findNavController().navigate(R.id.action_uikitSampleFragment_to_thirdDiagramFragment)
        }
        binding.fourthDiagramButton.setOnClickListener {
            findNavController().navigate(R.id.action_uikitSampleFragment_to_fourthDiagramFragment)
        }
        binding.fifthDiagramButton.setOnClickListener {
            findNavController().navigate(R.id.action_uikitSampleFragment_to_fifthDiagramFragment)
        }

        binding.testFragmentButton.setOnClickListener {
            findNavController().navigate(R.id.action_uikitSampleFragment_to_testFragment)
        }
    }

    override fun onSubscribe() {
        // example subscribe
    }

}