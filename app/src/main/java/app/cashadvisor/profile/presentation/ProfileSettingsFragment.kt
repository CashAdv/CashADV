package app.cashadvisor.profile.presentation

import androidx.fragment.app.viewModels
import app.cashadvisor.common.ui.BaseFragment
import app.cashadvisor.databinding.FragmentProfileSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSettingsFragment() :
    BaseFragment<FragmentProfileSettingsBinding, ProfileSettingsViewModel>(
        FragmentProfileSettingsBinding::inflate
    ) {
    override val viewModel: ProfileSettingsViewModel by viewModels()

    override fun onConfigureViews() {
        TODO("Not yet implemented")
    }

    override fun onSubscribe() {
        TODO("Not yet implemented")
    }
}