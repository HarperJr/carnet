package com.harper.carnet.data.diagnostics.server

import com.harper.carnet.BuildConfig
import com.harper.carnet.data.assets.AssetsManager
import com.harper.carnet.data.diagnostics.Commands
import com.harper.carnet.data.diagnostics.Connection
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.net.ServerSocket
import java.util.concurrent.atomic.AtomicReference

/**
 * Created by HarperJr on 23:59
 **/
class DiagnosticsServer(private val assetsManager: AssetsManager) {
    private val serverSocketRef: AtomicReference<ServerSocket> = AtomicReference()
    private val connections: MutableList<Connection> = mutableListOf()
    private val serverSocket: ServerSocket?
        get() = serverSocketRef.get()

    private var listenerDisposable: Disposable = Disposables.disposed()

    fun start(diagnosticsFileName: String) = Completable.fromAction {
        serverSocketRef.set(ServerSocket(BuildConfig.DIAGNOSTICS_PORT))
    }.doOnComplete {
        listenConnections(diagnosticsFileName)
    }.subscribeOn(Schedulers.io())

    private fun listenConnections(diagnosticsFileName: String) {
        listenerDisposable.dispose()
        listenerDisposable = Observable.create<Connection> { sub ->
            serverSocket?.let { socket ->
                while (true) {
                    val clientConnection = Connection(socket.accept())
                        .also { connections.add(it) }
                    sub.onNext(clientConnection)
                }
            }
        }.subscribeOn(Schedulers.io())
            .flatMapCompletable { connection ->
                Completable.fromAction {
                    val diagnosticsReader = assetsManager.getFileAsInputStream(diagnosticsFileName)?.bufferedReader()
                    diagnosticsReader?.let { reader ->
                        val deviceIdentity = runCatching { reader.readLine() }
                            .getOrDefault("")
                        connection.writeUTF8String(deviceIdentity)
                        outer@ while (true) {
                            when (connection.readUTF8String()) {
                                Commands.BEGIN_READING -> {
                                    val diagnosticsLine = runCatching { reader.readLine() }.getOrNull()
                                    while (diagnosticsLine != null && diagnosticsLine.isNotEmpty()) {
                                        if (diagnosticsLine == "begin")
                                            continue
                                        connection.writeUTF8String(diagnosticsLine)
                                    }
                                    break@outer
                                }
                                Commands.END_READING -> break@outer
                            }
                        }
                        diagnosticsReader.close()

                    }
                }
            }.subscribe({
                Timber.d("Diagnostics is read")
            }, { Timber.e(it) })
    }
}