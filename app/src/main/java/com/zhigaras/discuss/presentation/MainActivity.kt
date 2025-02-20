package com.zhigaras.discuss.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zhigaras.core.launchAndRepeatOn
import com.zhigaras.discuss.R
import com.zhigaras.discuss.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        launchAndRepeatOn(Lifecycle.State.STARTED) {
            viewModel.observeNavigation { it.show(supportFragmentManager, R.id.container) }
        }
        launchAndRepeatOn(Lifecycle.State.STARTED) {
            viewModel.init(savedInstanceState == null)
        }
    }
}
