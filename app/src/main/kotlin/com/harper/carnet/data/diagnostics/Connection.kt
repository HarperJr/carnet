package com.harper.carnet.data.diagnostics

import io.reactivex.Completable
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

/**
 * Created by HarperJr on 21:30
 **/
class Connection(private val host: String, private val port: Int) {

    constructor(socket: Socket) : this(socket.inetAddress.hostAddress, socket.port) {
        this.socket = socket
        this.bufferedReader = inputStream?.bufferedReader()
        this.bufferedWriter = outputStream?.bufferedWriter()
    }

    val inputStream: InputStream?
        get() = socket?.getInputStream()
    val outputStream: OutputStream?
        get() = socket?.getOutputStream()

    private var bufferedReader: BufferedReader? = null
    private var bufferedWriter: BufferedWriter? = null

    private var socket: Socket? = null

    fun open(): Completable = Completable.fromAction {
        this.socket = Socket(host, port)
        this.bufferedReader = inputStream?.bufferedReader()
        this.bufferedWriter = outputStream?.bufferedWriter()
    }

    fun close(): Completable = Completable.fromAction {
        this.socket?.close()
    }

    fun readUTF8String(): String? {
        return bufferedReader?.readLine()
    }

    fun writeUTF8String(string: String) {
        bufferedWriter?.write("$string\n")
        bufferedWriter?.flush()
    }
}