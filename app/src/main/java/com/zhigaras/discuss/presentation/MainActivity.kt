package com.zhigaras.discuss.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhigaras.discuss.R
import com.zhigaras.discuss.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<MainViewModel>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        
        viewModel.observeNavigation(this) {
            it.show(supportFragmentManager, R.id.container)
        }
        viewModel.observeNetworkState(this) {
            it.update(binding)
        }
        viewModel.init(savedInstanceState == null, this)
    }
}