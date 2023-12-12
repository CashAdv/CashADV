package app.cashadvisor.authorization.presentation.ui.diagrams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import app.cashadvisor.databinding.FragmentFourthDiagramBinding
import kotlin.random.Random

class FourthDiagramFragment : Fragment() {

    private var _binding: FragmentFourthDiagramBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFourthDiagramBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.randomInt.setOnClickListener {
            with(binding) {
                customDiagramFourth.progress = Random.nextInt(-5, 105)
                textCustomDiagramFourth.text = customDiagramFourth.progress.toString()
                customM6.progress = Random.nextInt(-5, 105)
                textCustomM6.text = customM6.progress.toString()
                customM8.progress = Random.nextInt(-5, 105)
                textCustomM8.text = customM8.progress.toString()
                customM9.progress = Random.nextInt(-5, 105)
                textCustomM9.text = customM9.progress.toString()
            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                with(binding) {
                    customDiagramFourth.progress = progress
                    textCustomDiagramFourth.text = progress.toString()
                    customM6.progress = progress
                    textCustomM6.text = progress.toString()
                    customM8.progress = progress
                    textCustomM8.text = progress.toString()
                    customM9.progress = progress
                    textCustomM9.text = progress.toString()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}