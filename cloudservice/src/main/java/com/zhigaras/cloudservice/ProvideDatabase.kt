package com.zhigaras.cloudservice

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

interface ProvideDatabase {
    
    fun database(): DatabaseReference
    
    class Base : ProvideDatabase {

        override fun database(): DatabaseReference {
            return Firebase.database(DATABASE_URL).reference
        }
        
        companion object {
            private const val DATABASE_URL =
                "https://discuss-b336c-default-rtdb.europe-west1.firebasedatabase.app/"
        }
    }
}