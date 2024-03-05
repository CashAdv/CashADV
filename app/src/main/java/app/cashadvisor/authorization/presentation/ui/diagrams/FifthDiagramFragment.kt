package app.cashadvisor.authorization.presentation.ui.diagrams

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.cashadvisor.R
import app.cashadvisor.databinding.FragmentFifthDiagramBinding
import app.cashadvisor.databinding.FragmentFourthDiagramBinding

class FifthDiagramFragment : Fragment() {

    private var _binding: FragmentFifthDiagramBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFifthDiagramBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            if (
                binding.editTextText.text.isNotBlank() &&
                binding.editTextText2.text.isNotBlank()
            ) {
                binding.circleDiagramFive.setValue(
                    binding.editTextText.text.toString().toInt(),
                    binding.editTextText2.text.toString().toInt()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}