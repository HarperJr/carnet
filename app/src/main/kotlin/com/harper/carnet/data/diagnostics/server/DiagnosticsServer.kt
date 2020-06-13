package com.harper.carnet.data.diagnostics.server

import com.harper.carnet.data.assets.AssetsManager
import com.harper.carnet.data.storage.SettingsStorage
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.*
import io.ktor.util.KtorExperimentalAPI
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.io.writeStringUtf8
import kotlinx.coroutines.runBlocking

/**
 * Created by HarperJr on 23:59
 **/
class DiagnosticsServer(private val settingsStorage: SettingsStorage, private val assetsManager: AssetsManager) {
    private val socketSubject: BehaviorSubject<ServerSocket> = BehaviorSubject.create()

    @KtorExperimentalAPI
    fun start() = createTcpSocket()
        .doOnSuccess { socketSubject.onNext(it) }
        .flatMapObservable { socket ->
            Observable.create<Socket> { sub ->
                runCatching {
                    while (!socket.isClosed) {
                        sub.onNext(runBlocking { socket.accept() })
                    }
                }.onSuccess { sub.onComplete() }
                    .onFailure { sub.onError(it) }
            }.map { clientSocket ->
                val outputStream = clientSocket.openWriteChannel(true)
                val diagnosticsStream = assetsManager.getFileAsInputStream("*.bin")?.bufferedReader()
                while (!clientSocket.isClosed && diagnosticsStream != null) {
                    runBlocking { outputStream.writeStringUtf8(diagnosticsStream.readLine()) }
                }
            }.observeOn(Schedulers.io())
        }.flatMapCompletable { Completable.complete() }

    fun stop() = Completable.complete()
        .andThen(socketSubject)
        .flatMapCompletable { socket ->
            Completable.create { sub ->
                if (socket.isClosed)
                    sub.onComplete()
                runCatching {
                    socket.close()
                }.onSuccess { sub.onComplete() }
                    .onFailure { sub.onError(it) }
            }
        }

    @KtorExperimentalAPI
    private fun createTcpSocket() = Single.fromCallable {
        val socket = socketSubject.value
        if (socket == null || socket.isClosed) {
            runBlocking {
                aSocket(ActorSelectorManager(Dispatchers.IO)).tcp()
                    .bind(settingsStorage.getAdapterHost(), settingsStorage.getAdapterPort())
            }
        } else throw IllegalStateException("Socket is already opened")
    }
}