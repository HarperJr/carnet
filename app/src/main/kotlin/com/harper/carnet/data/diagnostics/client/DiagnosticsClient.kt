package com.harper.carnet.data.diagnostics.client

import com.harper.carnet.BuildConfig
import com.harper.carnet.data.diagnostics.Commands
import com.harper.carnet.data.diagnostics.Connection
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference

class DiagnosticsClient {
    private var connectionRef: AtomicReference<Connection> = AtomicReference()
    private val connection: Connection?
        get() = connectionRef.get()
    private val isConnectedSubject: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    private val diagnosticsSubject: PublishSubject<String> = PublishSubject.create()
    private var diagnosticsDisposable: Disposable = Disposables.disposed()

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
        .doOnSuccess { readDiagnostics() }

    fun getDiagnostics(): Observable<String> = diagnosticsSubject

    private fun readDiagnostics() {
        diagnosticsDisposable.dispose()
        diagnosticsDisposable = Completable.fromAction {
            connection?.writeUTF8String(Commands.BEGIN_READING)
        }.subscribeOn(Schedulers.io())
            .andThen(Observable.create<String> { sub ->
                sub.onNext("")
                connection?.let {
                    while (true) {
                        val rawValue = it.readUTF8String()
                        if (rawValue == null || rawValue == "end")
                            break
                        sub.onNext(rawValue)
                    }
                } ?: sub.onError(Exception("Connection is undefined"))
                sub.onComplete()
            }.observeOn(Schedulers.io())).doOnSubscribe {
                Timber.d("Begin to read diagnostics values")
            }.subscribe({
                diagnosticsSubject.onNext(it)
                Timber.d("Read diagnostics: $it")
            }, { Timber.e(it) }, {
                Timber.d("All diagnostics is read")
                diagnosticsSubject.onComplete()
            })
    }

    fun disconnect(): Completable = connection?.close() ?: Completable.complete()

    fun isConnected(): Observable<Boolean> = isConnectedSubject
}