package com.zhigaras.login.presentation.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.zhigaras.auth.AuthResultWrapper
import com.zhigaras.core.BaseFragment
import com.zhigaras.login.databinding.FragmentSignInBinding
import com.zhigaras.login.domain.LoginRoutes
import com.zhigaras.login.presentation.resetpassword.ResetPasswordDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : BaseFragment<FragmentSignInBinding>() {
    
    private val viewModel by viewModel<SignInViewModel>()
    private val signInWithGoogleLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.handleResult(AuthResultWrapper.Base(it))
        }
    
    override fun initBinding(inflater: LayoutInflater) = FragmentSignInBinding.inflate(inflater)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val inputList = listOf(binding.emailInput.root, binding.passwordInput.root)
        
        binding.signInWithPassword.setOnClickListener {
            val isAllValid = inputList.map { it.isValid() }.all { it }
            if (isAllValid) {
                viewModel.signIn(inputList.first().text(), inputList.last().text())
            }
        }
        binding.signUpWithPassword.setOnClickListener {
            viewModel.navigateToSignUp(binding.emailInput.root.makeBundle(LoginRoutes.EMAIL_KEY))
        }
        binding.forgotPasswordText.setOnClickListener {
            ResetPasswordDialog().also {
                it.arguments = binding.emailInput.root.makeBundle(LoginRoutes.EMAIL_KEY)
                it.show(parentFragmentManager, it.tag)
            }
        }
        binding.signInWithGoogle.setOnClickListener {
            viewModel.startGoogleSignIn(signInWithGoogleLauncher)
        }
        
        viewModel.observe(this) {// TODO: move to baseFragment
            it.update(binding)
        }
    }
}