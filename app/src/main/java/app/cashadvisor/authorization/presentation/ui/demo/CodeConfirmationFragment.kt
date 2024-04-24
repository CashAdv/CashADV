package app.cashadvisor.authorization.presentation.ui.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.cashadvisor.databinding.FragmentCodeConfirmationBinding


class CodeConfirmationFragment : Fragment() {
    private var _binding: FragmentCodeConfirmationBinding? = null
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
        _binding = FragmentCodeConfirmationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.codeView.setCallback { code ->
            if (code.length == 4) {
                Toast.makeText(requireContext(), "Full entered $code", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Not full entered $code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}