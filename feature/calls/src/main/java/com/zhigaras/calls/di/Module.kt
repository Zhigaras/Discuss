package com.zhigaras.calls.di

import com.zhigaras.calls.data.CallsCloudServiceImpl
import com.zhigaras.calls.domain.CallCommunication
import com.zhigaras.calls.domain.CallsCloudService
import com.zhigaras.calls.domain.CallsController
import com.zhigaras.calls.domain.InitCalls
import com.zhigaras.calls.domain.MatchingInteractor
import com.zhigaras.calls.ui.CallViewModel
import com.zhigaras.calls.webrtc.IceServersList
import com.zhigaras.calls.webrtc.MyPeerConnectionFactory
import com.zhigaras.calls.webrtc.PeerConnectionCallback
import com.zhigaras.calls.webrtc.PeerConnectionCommunication
import com.zhigaras.calls.webrtc.PeerConnectionObserveWrapper
import com.zhigaras.calls.webrtc.WebRtcClient
import com.zhigaras.messaging.di.messagesModule
import com.zhigaras.messaging.domain.MessagesInteractor
import com.zhigaras.messaging.domain.Messaging
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import org.webrtc.Camera2Enumerator
import org.webrtc.EglBase
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory

/**
 * scope = CallsController, WebRTCClient, EglBase.Context, PeerConnectionFactory??
 *
 */

fun callModule() = listOf(messagesModule(), module {

//    viewModelOf(::CallViewModel)
    
    scope(named("messages")) {
        scoped {
            CallsController.Base(get(), androidApplication(), get(), get(), get(), get())
        } binds arrayOf(
            CallsController::class,
            InitCalls::class,
            Messaging::class
        )
        
        scoped { CallCommunication.Base() } binds arrayOf(
            CallCommunication.Mutable::class,
            CallCommunication.Observe::class,
            CallCommunication.Post::class
        )
        
        scoped { EglBase.create().eglBaseContext } bind EglBase.Context::class
    }
    
    viewModel {
        val initCalls = getKoin().getScope("messages").get<InitCalls>()
        val callsController = getKoin().getScope("messages").get<CallsController>()
        val communication = getKoin().getScope("messages").get<CallCommunication.Mutable>()
        CallViewModel(initCalls, callsController, get(), get(), communication, get(), get())
    }
    
    factory {
        val messaging = getKoin().getScope("messages").get<Messaging>()
        MessagesInteractor.Base(messaging)
    } bind MessagesInteractor::class
    
    
    
    factory { MatchingInteractor.Base(get()) } bind MatchingInteractor::class
    
    factory { CallsCloudServiceImpl(get()) } bind CallsCloudService::class
    
    factory {
        val communication = getKoin().getScope("messages").get<CallCommunication.Mutable>()
        PeerConnectionCallback(communication)
    } bind PeerConnectionCallback::class
})

fun webRtcModule() = module {
    
    factory { PeerConnectionCommunication.Base() } binds arrayOf(
        PeerConnectionCommunication.Mutable::class,
        PeerConnectionCommunication.Observe::class,
        PeerConnectionCommunication.Post::class
    )
    
    factory { PeerConnectionObserveWrapper(get(), get()) }
    
    factory {
        IceServersList(
            arrayListOf(
                PeerConnection.IceServer.builder("turn:a.relay.metered.ca:443?transport=tcp")
                    .setUsername("83eebabf8b4cce9d5dbcb649")
                    .setPassword("2D7JvfkOQtBdYW3R").createIceServer()
            )
        )
    } bind IceServersList::class
    
    factory {
        PeerConnectionFactory.Options().apply {
            disableEncryption = false
            disableNetworkMonitor = false
        }
    }
    
    factory { Camera2Enumerator(androidApplication()) }
    
    factory {
        val eglContext = getKoin().getScope("messages").get<EglBase.Context>()
        MyPeerConnectionFactory(androidApplication(), eglContext, get())
    }
    
    factory {
        val eglContext = getKoin().getScope("messages").get<EglBase.Context>()
        WebRtcClient(get(), get(), eglContext, get(), get())
    }
    
}