package app.cashadvisor.customView.authorization

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import app.cashadvisor.uikit.R
import app.cashadvisor.uikit.databinding.ItemStepBinding

class CustomViewStep @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    parent: ViewGroup? = null,

    private val stepNumber: Int = 1
) : AppCompatTextView(context, attrs, defStyleAttr) {

    val binding: ItemStepBinding

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.item_step, parent, false)
        binding = ItemStepBinding.bind(this)
        initializeView()
    }

    private fun initializeView() {
        binding.tvStep.text = stepNumber.toString()
    }
}