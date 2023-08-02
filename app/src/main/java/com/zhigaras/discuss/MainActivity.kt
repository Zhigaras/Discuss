package com.zhigaras.discuss

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhigaras.login.presentation.SignInFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SignInFragment())
                .commit()
    }
}