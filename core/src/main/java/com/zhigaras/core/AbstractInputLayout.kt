package com.zhigaras.core

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout


abstract class AbstractInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr), InputValidation {
    
    protected abstract val errorMessageId: Int
    private val textWatcher: TextWatcher = BaseTextWatcher { error = "" }
    
    protected abstract fun innerIsValid(): Boolean
    
    override fun text(): String = (editText?.text ?: "").toString()
    
    override fun addTextWatcher(textWatcher: TextWatcher) {
        editText?.addTextChangedListener(textWatcher)
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
    
    fun setText(text: String?) {
        editText?.setText(text ?: "")
    }
    
    fun makeBundle(key: String) = Bundle().also { it.putString(key, text()) }
    
    fun setError(@StringRes messageId: Int) {
        error = context.getString(messageId)
    }
}

interface InputValidation {
    
    fun isValid(): Boolean
    
    fun addTextWatcher(textWatcher: TextWatcher)
    
    fun text(): String
}