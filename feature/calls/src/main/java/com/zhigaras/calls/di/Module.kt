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
import com.zhigaras.calls.webrtc.MyPeerConnectionObserver
import com.zhigaras.calls.webrtc.PeerConnectionCallback
import com.zhigaras.calls.webrtc.PeerConnectionCommunication
import com.zhigaras.calls.webrtc.WebRtcClient
import com.zhigaras.messaging.di.messagesModule
import com.zhigaras.messaging.domain.Messaging
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
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
    
    viewModelOf(::CallViewModel)
    
    single { CallCommunication.Base() } binds arrayOf(
        CallCommunication.Mutable::class,
        CallCommunication.Observe::class,
        CallCommunication.Post::class
    )
    
    single {
        CallsController.Base(androidApplication(), get(), get(), get(), get(), get())
    } binds arrayOf(
        CallsController::class,
        InitCalls::class,
        Messaging::class
    )
    
    factory { MatchingInteractor.Base(get()) } bind MatchingInteractor::class
    
    factory { CallsCloudServiceImpl(get()) } bind CallsCloudService::class
    
    factory { PeerConnectionCallback(get()) } bind PeerConnectionCallback::class
})

fun webRtcModule() = module {
    
    single { PeerConnectionCommunication.Base() } binds arrayOf(
        PeerConnectionCommunication.Mutable::class,
        PeerConnectionCommunication.Observe::class,
        PeerConnectionCommunication.Post::class
    )
    
    factory { MyPeerConnectionObserver(get(), get()) }
    
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
    
    single { EglBase.create().eglBaseContext } bind EglBase.Context::class
    
    factory { MyPeerConnectionFactory(androidApplication(), get(), get()) }
    
    single { WebRtcClient(get(), get(), get(), get(), get()) }
    
}