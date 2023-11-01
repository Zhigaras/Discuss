package com.zhigaras.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface Screen {
    
    fun show(fragmentManager: FragmentManager, containerId: Int)
    
    abstract class ReplaceAndAddToBackstack(
        private val className: Class<out Fragment>,
        private val args: Bundle? = null
    ) : Screen {
        
        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.beginTransaction()
                .replace(containerId, className.newInstance().also { it.arguments = args })
                .addToBackStack(className.simpleName)
                .commit()
        }
    }
    
    abstract class Replace(
        private val className: Class<out Fragment>,
        private val args: Bundle? = null
    ) : Screen {
        
        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.beginTransaction()
                .replace(containerId, className.newInstance().also { it.arguments = args })
                .commit()
        }
    }
    
    abstract class ReplaceWithClearBackstack(
        private val className: Class<out Fragment>,
        private val args: Bundle? = null
    ) : Screen {
        
        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            fragmentManager.beginTransaction()
                .replace(containerId, className.newInstance().also { it.arguments = args })
                .commit()
        }
    }
    
    object PopBackStack : Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.popBackStack()
        }
    }
}