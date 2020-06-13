package com.harper.carnet.data.api

/**
 * Created by HarperJr on 11:20
 **/
class ApiException(val code: Int, override val message: String) : Throwable(message) {
    companion object {
        fun letFromThrowable(throwable: Throwable, action: ((message: String) -> Unit)? = null): ApiException? {
            if (throwable is ApiException) {
                action?.invoke(throwable.message)
                return throwable
            }
            return null
        }
    }
}