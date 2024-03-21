package app.cashadvisor.signup.presentation.ui

import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.cashadvisor.R
import app.cashadvisor.common.ui.BaseFragment
import app.cashadvisor.databinding.FragmentSignupBinding
import app.cashadvisor.signup.presentation.viewmodel.SignupViewModel

class SignupFragment:
        BaseFragment<FragmentSignupBinding, SignupViewModel>(FragmentSignupBinding::inflate){

        override val viewModel: SignupViewModel by viewModels()
    override fun onConfigureViews() {
        binding.btnBack.setOnClickListener(){
            findNavController().navigateUp()
        }

        binding.btnCreateAccount.setOnClickListener(){
            findNavController().navigate(R.id.action_signupFragment_to_entryVerificationFragment)
        }

        binding.edittextEnterEmail.addTextChangedListener (object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                
            }

        })

        binding.edittextComeUpWithAPassword.addTextChangedListener {  }

        binding.edittextConfirmThePassword.addTextChangedListener {  }
    }

    override fun onSubscribe() {
        TODO("Not yet implemented")
    }
}