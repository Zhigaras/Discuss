package com.zhigaras.calls.domain.model

import android.util.Log
import com.zhigaras.calls.domain.CallsController

data class ConnectionData(
    val target: String = "",
    val sender: String = "",
    val offer: MySessionDescription? = null,
    val answer: MySessionDescription? = null,
    val iceCandidate: MyIceCandidate? = null

//    val data: String = "",
//    val type: ConnectionDataType = ConnectionDataType.EMPTY
) : HandleConnectionData {
    override fun handle(controller: CallsController) {
//        type.handle(controller, this)
        
        if (offer != null) {
            Log.d("QQQ", offer.toString())
            controller.sendAnswer(offer, sender, target)
            return
        }
        if (answer != null) {
            Log.d("QQQ", answer.toString())
            controller.handleAnswer(answer)
            return
        }
        if (iceCandidate != null) {
            Log.d("QQQ", iceCandidate.toString())
            controller.handleIceCandidate(iceCandidate)
            return
        }
    }
}

interface HandleConnectionData {
    
    fun handle(controller: CallsController)
}

//enum class ConnectionDataType {
//
//    EMPTY {
//        override fun handle(controller: CallsController, connectionData: ConnectionData) {
//            Log.d("AAA trouble empty data", "nothing to do")
//        }
//    },
//    OFFER {
//        override fun handle(controller: CallsController, connectionData: ConnectionData) {
//            Log.d("AAA trouble OFFER handle", "offer handled")
//            client.onRemoteSessionReceived(
//                SessionDescription(
//                    SessionDescription.Type.OFFER, connectionData.data
//                )
//            )
//            client.answer(connectionData.sender, connectionData.target)
//        }
//    },
//    ANSWER {
//        override fun handle(controller: CallsController, connectionData: ConnectionData) {
//            Log.d("AAA trouble ANSWER handle", "answer handled")
//            client.onRemoteSessionReceived(
//                SessionDescription(
//                    SessionDescription.Type.ANSWER, connectionData.data
//                )
//            )
//        }
//    },
//    ICE_CANDIDATE {
//        override fun handle(controller: CallsController, connectionData: ConnectionData) {
//            Log.d("AAA trouble CANDIDATE handle", "iceCandidate handled")
//            try {
//                val candidate: IceCandidate =
//                    gson.fromJson(connectionData.data, IceCandidate::class.java)
//                client.addIceCandidate(candidate)
//            } catch (e: Exception) {
//                Log.d("AAA trouble CANDIDATE error", e.message.toString()) // TODO: fix this
//            }
//        }
//    };
//
//    val gson: Gson = Gson() // TODO: how to escape of it??
//
//    abstract fun handle(controller: CallsController, connectionData: ConnectionData)
//}