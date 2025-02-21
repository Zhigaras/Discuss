package com.zhigaras.core

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.launchAndRepeatOn(state: Lifecycle.State, block: suspend CoroutineScope.() -> Unit) =
    getViewLifecycleOwner().lifecycleScope.launch { repeatOnLifecycle(state, block) }

fun AppCompatActivity.launchAndRepeatOn(state: Lifecycle.State, block: suspend CoroutineScope.() -> Unit) =
    lifecycleScope.launch { repeatOnLifecycle(state, block) }
