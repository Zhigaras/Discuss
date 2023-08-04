package com.zhigaras.login.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.zhigaras.core.presentation.BaseFragment
import com.zhigaras.login.databinding.FragmentSignInBinding
import kotlinx.coroutines.launch

class SignInFragment : BaseFragment<FragmentSignInBinding>() {
    
    private val viewModel by lazy { initViewModel<SignInViewModel>() }
    
    override fun initBinding(inflater: LayoutInflater) = FragmentSignInBinding.inflate(inflater)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.signInWithPassword.setOnClickListener {
            viewModel.signIn(binding.emailInputLayout.text(), binding.passwordInputLayout.text())
        }
        
        lifecycleScope.launch {
            viewModel.liveData.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}