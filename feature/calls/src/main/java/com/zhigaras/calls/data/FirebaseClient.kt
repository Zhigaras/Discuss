package com.zhigaras.calls.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.calls.webrtc.ErrorCallBack
import com.zhigaras.calls.webrtc.NewEventCallBack
import com.zhigaras.calls.webrtc.SuccessCallBack
import java.util.Objects

class FirebaseClient {
    private val gson = Gson()
    private val dbRef: DatabaseReference =
        Firebase.database("https://discuss-b336c-default-rtdb.europe-west1.firebasedatabase.app/").reference
    private val currentUsername: String = FirebaseAuth.getInstance().uid ?: "no id"
    fun login(username: String, callBack: SuccessCallBack) {
//        dbRef.child(username).setValue("")
//            .addOnCompleteListener {
//                currentUsername = username
                callBack.onSuccess()
//            }
    }
    
    fun sendMessageToOtherUser(connectionData: ConnectionData, errorCallBack: ErrorCallBack) {
        dbRef.child("Users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(connectionData.target).exists()) {
                    dbRef.child("Users").child(connectionData.target).child(LATEST_EVENT_FIELD_NAME)
                        .setValue(gson.toJson(connectionData))
                } else {
                    errorCallBack.onError()
                }
            }
            
            override fun onCancelled(error: DatabaseError) {
                errorCallBack.onError()
            }
        })
    }
    
    fun observeIncomingLatestEvent(callBack: NewEventCallBack) {
        dbRef.child("Users").child(currentUsername).child(LATEST_EVENT_FIELD_NAME).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val data = Objects.requireNonNull<Any>(snapshot.value).toString()
                        val connectionData: ConnectionData = gson.fromJson(data, ConnectionData::class.java)
                        callBack.onNewEventReceived(connectionData)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                
                override fun onCancelled(error: DatabaseError) {}
            }
        )
    }
    
    companion object {
        private const val LATEST_EVENT_FIELD_NAME = "connectionEvent"
    }
}