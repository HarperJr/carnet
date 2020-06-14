package com.harper.carnet.data.diagnostics.client

import com.harper.carnet.BuildConfig
import com.harper.carnet.data.diagnostics.Connection
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.atomic.AtomicReference

class DiagnosticsClient {
    private var connectionRef: AtomicReference<Connection> = AtomicReference()
    private val connection: Connection?
        get() = connectionRef.get()
    private val isConnectedSubject: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    fun connect(): Single<String> = Single.fromCallable {
        if (isConnectedSubject.value == false) {
            Connection(BuildConfig.DIAGNOSTICS_HOST, BuildConfig.DIAGNOSTICS_PORT)
        } else throw Exception("Connection is already settled")
    }.subscribeOn(Schedulers.io())
        .doOnSuccess { connectionRef.set(it) }
        .flatMapCompletable { it.open() }
        .andThen(Single.create<String> { sub ->
            val deviceIdentity = connection?.readUTF8String()
            if (deviceIdentity == null || deviceIdentity.isEmpty()) {
                sub.onError(Exception("Unable to get device identity, wrong stream is detected"))
            } else sub.onSuccess(deviceIdentity)
        }.observeOn(Schedulers.io())).doOnSuccess { isConnectedSubject.onNext(true) }

    fun disconnect(): Completable = connection?.close() ?: Completable.complete()

    fun isConnected(): Observable<Boolean> = isConnectedSubject
}