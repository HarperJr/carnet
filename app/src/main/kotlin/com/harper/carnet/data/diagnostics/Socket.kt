package com.harper.carnet.data.diagnostics

import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.io.ByteWriteChannel

/**
 * Created by HarperJr on 1:32
 **/
class Socket<T : Socket>(
    val socket: T,
    val inputStream: ByteReadChannel = socket.openReadChannel(),
    val writeStream: ByteWriteChannel = socket.openWriteChannel(true)
)