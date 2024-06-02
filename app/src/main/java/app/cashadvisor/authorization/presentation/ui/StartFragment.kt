package app.cashadvisor.authorization.presentation.ui

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import app.cashadvisor.R
import app.cashadvisor.analytics.data.db.MainDb
import app.cashadvisor.analytics.data.db.asCategoryEntity
import app.cashadvisor.analytics.data.db.asUserAnalyticsEntity
import app.cashadvisor.analytics.data.db.test.TestCategoryResponse
import app.cashadvisor.analytics.data.db.test.TestUserAnalyticsResponse
import app.cashadvisor.authorization.presentation.ui.models.StartScreenEvents
import app.cashadvisor.authorization.presentation.ui.models.StartScreenSideEffects
import app.cashadvisor.authorization.presentation.ui.models.StartScreenUiState
import app.cashadvisor.authorization.presentation.viewmodel.StartViewModel
import app.cashadvisor.common.ui.BaseFragment
import app.cashadvisor.databinding.FragmentStartBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StartFragment :
    BaseFragment<FragmentStartBinding, StartViewModel>(FragmentStartBinding::inflate) {
    override val viewModel: StartViewModel by viewModels()
    override fun onConfigureViews() {

        ///////////   DB Test    //////////////////

        val testCategoryResponse = TestCategoryResponse().categorySettingsDto
        val testUserAnalyticsResponse = TestUserAnalyticsResponse().userAnalyticsDto
        val categoryEntityList = testCategoryResponse.asCategoryEntity()
        val userAnalyticsEntityList = testUserAnalyticsResponse.asUserAnalyticsEntity()

        val db = MainDb.getDb(requireContext())
        val dao = db.getDao()

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            dao.upsertCategoryEntity(categoryEntityList)
            dao.upsertUserAnalyticsEntity(userAnalyticsEntityList)
        }

        /////////////////////////////////

        binding.containerLayout.setOnClickListener {
            viewModel.handleEvent(StartScreenEvents.ProceedNextClicked)
        }

        binding.tapToUiKitSampleTextView.setOnClickListener {
            viewModel.handleEvent(StartScreenEvents.UiKitClicked)
        }
    }

    override fun onSubscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    updateUi(uiState)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sideEffects.collect { sideEffect ->
                    handleSideEffect(sideEffect)
                }
            }
        }
    }

    private fun updateUi(uiState: StartScreenUiState) {
        when (uiState.state) {

            StartScreenUiState.UiState.Loading -> {}

            is StartScreenUiState.UiState.Success -> {
                binding.tapToUiKitSampleTextView.isVisible = uiState.state.isDebug
            }

            StartScreenUiState.UiState.Error -> {}
        }
    }

    private fun handleSideEffect(sideEffect: StartScreenSideEffects) {
        when (sideEffect) {
            StartScreenSideEffects.NavigateToAuthenticationScreen -> {
                findNavController().navigate(R.id.action_startFragment_to_entryFragment)

            }

            StartScreenSideEffects.NavigateToPinCodeScreen -> {
                findNavController().navigate(R.id.action_startFragment_to_pinCodeFragment)
            }


            StartScreenSideEffects.NavigateToUiKitScreen -> {
                findNavController().navigate(R.id.action_startFragment_to_uikitSampleFragment)
            }

            is StartScreenSideEffects.ShowToast -> {
                Toast.makeText(
                    requireContext(), getText(sideEffect.message), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}