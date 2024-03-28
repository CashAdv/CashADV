package app.cashadvisor.customView.authorization

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import app.cashadvisor.customView.authorization.models.StepType
import app.cashadvisor.uikit.R
import app.cashadvisor.uikit.databinding.ComponentStepsBinding


class CustomViewSteps @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: ComponentStepsBinding
    private var maxSteps = 1
    private var currentStep = 1

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.component_steps, this, true)
        binding = ComponentStepsBinding.bind(this)
        initializeAttributes(attrs, defStyleAttr, defStyleRes)
    }

    private fun initializeAttributes(
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomViewSteps,
            defStyleAttr,
            defStyleRes
        )

        val maxSteps = typedArray.getInt(R.styleable.CustomViewSteps_maxSteps, 1)
        val currentStep = typedArray.getInt(R.styleable.CustomViewSteps_currentStep, 1)

        fillLayoutWithSteps(maxSteps, currentStep)

        typedArray.recycle()
    }

    private fun createStep(
        stepNumber: Int,
        defStyleAttr: Int,
        layoutParams: LayoutParams?
    ): CustomViewStep {
        val step = CustomViewStep(
            context = context,
            stepNumber = stepNumber,
            defStyleAttr = defStyleAttr,
            parent = this
        )
        layoutParams?.let {
            step.layoutParams = it
        }
        return step
    }

    private fun fillLayoutWithSteps(maxSteps: Int, currentStep: Int) {

        val maxStepsValidated = validateMaxSteps(maxSteps)
        val currentStepValidated = validateCurrentStep(currentStep,maxSteps)

        this.maxSteps = maxStepsValidated
        this.currentStep = currentStepValidated

        repeat(maxStepsValidated) {
            val stepPosition = it + 1
            val stepType = when {
                stepPosition == currentStepValidated -> StepType.ACTIVE
                stepPosition < currentStepValidated -> StepType.COMPLETED
                else -> StepType.INACTIVE
            }
            val layoutParams = if (stepPosition != maxStepsValidated) {
                LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd =
                        resources.getDimension(R.dimen.login_screen_step_inside_margin).toInt()
                }
            } else {
                null
            }
            val newStep = when (stepType) {
                StepType.ACTIVE -> {
                    createStep(stepPosition, R.attr.CustomStepActive, layoutParams)
                }

                StepType.INACTIVE -> {
                    createStep(stepPosition, R.attr.CustomStepInactive, layoutParams)
                }

                StepType.COMPLETED -> {
                    createStep(stepPosition, R.attr.CustomStepCompleted, layoutParams)
                }
            }
            this.addView(newStep)
        }
    }

    /**
     * input values for maxSteps and currentStep must be >=1 and
     * maxSteps should be >= currentStep
     */
    fun changeSteps(maxSteps: Int, currentStep: Int) {
        removeAllViews()
        fillLayoutWithSteps(maxSteps, currentStep)
    }

    private fun validateMaxSteps(maxStepsInput: Int): Int {
        return if (maxStepsInput <= 0) {
            1
        } else {
            maxStepsInput
        }
    }

    private fun validateCurrentStep(currentStepInput: Int, maxStepsInput: Int): Int {
        return if (currentStepInput <= 0) {
            1
        } else if (currentStepInput > maxStepsInput) {
            maxStepsInput
        } else {
            currentStepInput
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()!!
        val savedState = SavedState(superState)
        savedState.maxSteps = maxSteps
        savedState.currentStep = currentStep
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        changeSteps(
            maxSteps = savedState.maxSteps,
            currentStep = savedState.currentStep
        )
    }

    class SavedState : BaseSavedState {
        var maxSteps = 1
        var currentStep = 1

        constructor(superState: Parcelable) : super(superState)
        constructor(parcel: Parcel) : super(parcel) {
            maxSteps = parcel.readInt()
            currentStep = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(maxSteps)
            out.writeInt(currentStep)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return Array(size) { null }
                }
            }
        }
    }
}