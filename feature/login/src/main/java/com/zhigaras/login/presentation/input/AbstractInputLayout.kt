package com.zhigaras.login.presentation.input

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout


abstract class AbstractInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr), InputValidation, Init {
    
    protected abstract val errorMessageId: Int
    protected abstract var textWatcher: TextWatcher
    
    protected abstract fun innerIsValid(): Boolean
    
    fun text(): String = (editText?.text ?: "").toString()
    
    override fun initTextWatcher(textWatcher: TextWatcher) {
        this.textWatcher = textWatcher
    }
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        editText?.addTextChangedListener(textWatcher)
    }
    
    override fun isValid(): Boolean {
        val isValid = innerIsValid()
        error = if (isValid) "" else context.getString(errorMessageId)
        return isValid
    }
    
    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle().also {
            it.putParcelable("state", super.onSaveInstanceState())
            it.putString("text", editText!!.text.toString())
        }
        return bundle
    }
    
    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            editText!!.setText(state.getString("text"))
            super.onRestoreInstanceState(state.getParcelable("state"))
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}

interface InputValidation {
    
    fun isValid(): Boolean
}

interface Init {
    
    fun initTextWatcher(textWatcher: TextWatcher)
}