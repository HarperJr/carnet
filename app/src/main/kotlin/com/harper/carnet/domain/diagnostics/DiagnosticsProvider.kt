package com.harper.carnet.domain.diagnostics

import com.harper.carnet.data.diagnostics.client.DiagnosticsClient
import com.harper.carnet.data.diagnostics.server.DiagnosticsServer
import io.ktor.util.KtorExperimentalAPI
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by HarperJr on 12:37
 **/
class DiagnosticsProvider(
    private val diagnosticsServer: DiagnosticsServer,
    private val diagnosticsClient: DiagnosticsClient
) {

    @KtorExperimentalAPI
    fun connect(deviceIdentity: String): Single<String> {
        return diagnosticsServer.start("device_$deviceIdentity.bin")
            .andThen(diagnosticsClient.connect())
            .onErrorResumeNext(diagnosticsClient.connect())
    }

    fun isConnected(): Observable<Boolean> {
        return diagnosticsClient.isConnected()
    }
}