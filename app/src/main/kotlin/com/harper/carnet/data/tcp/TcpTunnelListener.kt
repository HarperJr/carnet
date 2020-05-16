package com.harper.carnet.data.tcp

interface TcpTunnelListener {

    fun onConnected()

    fun onReceive(bytes: ByteArray)

    fun onError(throwable: Throwable)
}