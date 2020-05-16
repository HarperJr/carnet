package com.harper.carnet.data.tcp

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean

class TcpTunnel(private val host: String, private val port: Int) {
    private val tcpConnectionListeners = mutableListOf<TcpTunnelListener>()
    private val tcpSocket: Socket = Socket()
    private val isConnected = AtomicBoolean()

    private var connectionStateDisposable = Disposables.disposed()

    private var connectionDisposable = Disposables.disposed()

    fun listen(listener: TcpTunnelListener) {
        tcpConnectionListeners.add(listener)
    }

    fun unsubscribe(listener: TcpTunnelListener) {
        tcpConnectionListeners.remove(listener)
    }

    private fun connect() = Completable
        .fromAction {
            try {
                tcpSocket.connect(InetSocketAddress(host, port), TIMEOUT)
                isConnected.set(true)
            } catch (ex: Exception) {
                isConnected.set(false)
            }
        }
        .subscribeOn(Schedulers.io())
        .doOnComplete { tcpConnectionListeners.forEach { it.onConnected() } }
        .doOnError {
            connectionStateDisposable.dispose()
            connectionStateDisposable = disconnect()
        }
        .andThen(connectionObservable)
        .subscribe({ bytes ->
            tcpConnectionListeners.forEach { it.onReceive(bytes) }
        }, { throwable ->
            tcpConnectionListeners.forEach { it.onError(throwable) }
        })

    private fun disconnect() = Completable
        .fromAction {
            if (isConnected.compareAndSet(true, false))
                tcpSocket.close()
        }
        .subscribeOn(Schedulers.io())
        .subscribe { connectionDisposable.dispose() }

    private fun isConnected() = isConnected.get()

    private val connectionObservable = Observable
        .create<ByteArray> { subscriber ->
            try {
                val socketInputStream = tcpSocket.getInputStream()
                val byteBuffer = ByteArray(1024)
                while (isConnected()) {
                    while (socketInputStream.available() > 0) {
                        if ((socketInputStream.read(byteBuffer)) != -1) {
                            subscriber.onNext(byteBuffer)
                        }
                    }
                }
            } catch (ex: Exception) {
                subscriber.onError(ex)
                disconnect()
            }
        }
        .observeOn(Schedulers.io())

    companion object {
        private const val TIMEOUT = 10000
    }
}