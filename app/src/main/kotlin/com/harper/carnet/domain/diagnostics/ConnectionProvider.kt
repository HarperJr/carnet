package com.harper.carnet.domain.diagnostics

import com.harper.carnet.data.diagnostics.client.DiagnosticsClient
import io.reactivex.Single

/**
 * Created by HarperJr on 12:37
 **/
class ConnectionProvider(private val diagnosticsClient: DiagnosticsClient) {

    fun provideConnection(): Single<Result> {
        return diagnosticsClient.connect()
            .map { deviceIdentity ->
                if (deviceIdentity.isNotEmpty()) {
                    Result(deviceIdentity, true)
                } else Result(null, false)
            }
    }

    class Result(val deviceIdentity: String?, val isConnected: Boolean)
}