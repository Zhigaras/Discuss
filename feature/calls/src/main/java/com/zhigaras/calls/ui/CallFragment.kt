package com.zhigaras.calls.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.BundleCompat
import com.zhigaras.calls.domain.CallRoutes
import com.zhigaras.calls.domain.model.ReadyToCallUser
import com.zhigaras.core.BaseFragment
import com.zhigaras.webrtc.databinding.FragmentCallBinding
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.getScopeId
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope

class CallFragment : BaseFragment<FragmentCallBinding>() {
    
    private val viewModel by viewModel<CallViewModel>()
    private val scope: Scope = getKoin().createScope("messages", named("messages"))
    init {
        Log.d("QQQQQ", scope.id)
        Log.d("QQQQQ", getKoin().getScope("messages").getScopeId())
    }
    
    override fun initBinding(inflater: LayoutInflater) = FragmentCallBinding.inflate(inflater)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel.init(binding.localView, binding.remoteView)
        if (savedInstanceState == null) {
            getUserFromArgs()?.let { viewModel.lookForOpponent(it) }
        }
        
        viewModel.observe(this) {
            it.update(binding)
        }
        
        binding.escapeButton.setOnClickListener {
            viewModel.closeConnection()
        }
        binding.nextButton.setOnClickListener {
            getUserFromArgs()?.let { viewModel.nextOpponent(it) }
        }
    }
    
    override fun onPause() {
        super.onPause()
        binding.waitingView.onPause()
    }
    
    override fun onResume() {
        super.onResume()
        binding.waitingView.onResume()
    }
    
    private fun getUserFromArgs(): ReadyToCallUser? {
        val args = arguments
        return if (args != null) {
            BundleCompat.getParcelable(
                args, CallRoutes.READY_TO_CALL_USER_KEY, ReadyToCallUser::class.java
            )
        } else null
    }
}