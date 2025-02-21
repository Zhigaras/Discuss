package com.zhigaras.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseDialog<VB : ViewBinding, VM : BaseViewModel<UiState<VB>>> : BottomSheetDialogFragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!
    protected open val viewModel: VM? = null

    protected abstract fun initBinding(inflater: LayoutInflater): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = initBinding(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchAndRepeatOn(Lifecycle.State.STARTED) {
            viewModel?.observeUiState { it.update(binding) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
