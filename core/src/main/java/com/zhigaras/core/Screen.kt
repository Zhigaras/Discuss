package com.zhigaras.core

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface Screen {
    
    fun show(fragmentManager: FragmentManager, containerId: Int)
    
    abstract class ReplaceAndAddToBackstack(private val className: Class<out Fragment>) : Screen {
        
        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.beginTransaction()
                .replace(containerId, className.newInstance())
                .addToBackStack(className.simpleName)
                .commit()
        }
    }
    
    abstract class Replace(private val className: Class<out Fragment>) : Screen {
        
        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.beginTransaction()
                .replace(containerId, className.newInstance())
                .commit()
        }
    }
    
    abstract class ReplaceWithClearBackstack(private val className: Class<out Fragment>) : Screen {
        
        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            fragmentManager.beginTransaction()
                .replace(containerId, className.newInstance())
                .commit()
        }
    }
}