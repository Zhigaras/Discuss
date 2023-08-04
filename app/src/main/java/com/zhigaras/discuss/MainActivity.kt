package com.zhigaras.discuss

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhigaras.login.presentation.signup.SignUpFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SignUpFragment())
                .commit()
    }
}