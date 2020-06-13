package com.harper.carnet.data.diagnostics.client

import com.harper.carnet.data.storage.SettingsStorage
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.isClosed
import io.ktor.network.sockets.openReadChannel
import io.ktor.util.KtorExperimentalAPI
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.io.readUTF8Line
import kotlinx.coroutines.runBlocking

class DiagnosticsClient(private val settingsStorage: SettingsStorage) {
    private val socketSubject: BehaviorSubject<Socket> = BehaviorSubject.create()
    private val dataSubject: PublishSubject<String> = PublishSubject.create()

    @KtorExperimentalAPI
    fun connect() = createTcpSocket()
        .observeOn(Schedulers.io())
        .doOnSuccess { socketSubject.onNext(it) }
        .flatMap { socket ->
            val iStream = socket.openReadChannel()
            Single.create<String> { sub ->
                var isDeviceIdRead = false
                while (!iStream.isClosedForRead) {
                    val line = runBlocking { iStream.readUTF8Line() }
                    if (line == null || line.isEmpty()) {
                        break
                    } else {
                        if (isDeviceIdRead) {
                            dataSubject.onNext(line)
                        } else {
                            isDeviceIdRead = true
                            sub.onSuccess(line)
                        }
                    }
                }
                dataSubject.onComplete()
            }.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
        }

    fun disconnect() = socketSubject
        .flatMapCompletable { socket ->
            Completable.create { sub ->
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
                    .connect(settingsStorage.getAdapterHost(), settingsStorage.getAdapterPort())
            }
        } else throw IllegalStateException("Socket is already opened")
    }
}