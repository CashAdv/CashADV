package app.cashadvisor.authorization.presentation.ui

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.cashadvisor.R
import app.cashadvisor.authorization.presentation.viewmodel.EntryViewModel
import app.cashadvisor.authorization.presentation.viewmodel.models.EntryInteraction
import app.cashadvisor.authorization.presentation.viewmodel.models.EntryScreenState
import app.cashadvisor.common.ui.BaseFragment
import app.cashadvisor.common.utils.debounce
import app.cashadvisor.databinding.FragmentEntryBinding
import kotlinx.coroutines.launch


class EntryFragment :
    BaseFragment<FragmentEntryBinding, EntryViewModel>(FragmentEntryBinding::inflate) {
    override val viewModel: EntryViewModel by viewModels()
    private var onButtonClickDebounce: ((EntryInteraction) -> Unit)? = null


    override fun onConfigureViews() {
        configureDebounce()
        binding.btnLogin.setOnClickListener {
            onButtonClickDebounce?.invoke(EntryInteraction.SignInTapped)
        }
        binding.btnSignup.setOnClickListener {
            onButtonClickDebounce?.invoke(EntryInteraction.SignUpTapped)
        }
    }

    override fun onSubscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                render(state)
            }
        }
    }

    private fun render(state: EntryScreenState) {
        when (state) {
            is EntryScreenState.SignUp -> {
                findNavController().navigate(R.id.action_entryFragment_to_signupFragment)

            }

            is EntryScreenState.SignIn -> {
                findNavController().navigate(R.id.action_entryFragment_to_loginFragment)

            }

            else -> {
                // no-op
            }
        }
    }

    fun configureDebounce() {
        onButtonClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            useLastParam = false,
            actionWithDelay = false
        ) { action ->
            viewModel.handleInteraction(action)
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }

}