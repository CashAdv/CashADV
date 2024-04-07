package app.cashadvisor.authorization.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.cashadvisor.R
import app.cashadvisor.authorization.presentation.viewmodel.passwordrecovery.PasswordRecoveryViewModel
import app.cashadvisor.common.ui.BaseFragment
import app.cashadvisor.databinding.FragmentPasswordRecoveryBinding

class PasswordRecoveryFragment() : BaseFragment<FragmentPasswordRecoveryBinding, PasswordRecoveryViewModel>(FragmentPasswordRecoveryBinding::inflate) {
    override val viewModel: PasswordRecoveryViewModel by viewModels()
    override fun onConfigureViews() {
        TODO("Not yet implemented")
    }

    override fun onSubscribe() {
        TODO("Not yet implemented")
    }


}