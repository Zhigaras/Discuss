package com.zhigaras.cloudeservice

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

interface ProvideDatabase {
    
    fun database(): DatabaseReference
    
    class Base(context: Context) : ProvideDatabase {
        
        init {
            FirebaseApp.initializeApp(context)
            Firebase.database(DATABASE_URL).setPersistenceEnabled(false)
        }
        
        override fun database(): DatabaseReference {
            return Firebase.database(DATABASE_URL).reference
        }
        
        companion object {
            private const val DATABASE_URL =
                "https://organizemeetup-2397f-default-rtdb.asia-southeast1.firebasedatabase.app"
        }
    }
}