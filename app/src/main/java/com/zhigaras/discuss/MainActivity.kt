package com.zhigaras.discuss

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    
    private val viewModel by viewModel<MainViewModel>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        viewModel.observe(this) {
            it.show(supportFragmentManager, R.id.container)
        }
        viewModel.init(savedInstanceState == null)
        
    }
}