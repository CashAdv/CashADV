package app.cashadvisor.authorization.presentation.ui.diagrams

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.cashadvisor.R
import app.cashadvisor.databinding.FragmentFirstDiagramBinding
import app.cashadvisor.databinding.FragmentUikitSampleBinding

class FirstDiagramFragment : Fragment() {

    private var _binding: FragmentFirstDiagramBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstDiagramBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calculateButton.setOnClickListener {
            val variableValue = binding.variableSourceEnterField.text.toString()
            val permanentValue = binding.permanentSourceEnterField.text.toString()

            if (variableValue.isNotBlank() && permanentValue.isNotBlank()) {
                binding.circularGraph.setNewValues(variableValue.toFloat(), permanentValue.toFloat())
            } else {
                Toast.makeText(requireContext(), "Fill in the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}