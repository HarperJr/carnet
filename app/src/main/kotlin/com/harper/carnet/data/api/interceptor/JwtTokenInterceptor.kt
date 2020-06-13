package com.harper.carnet.data.api.interceptor

import com.auth0.android.jwt.Claim
import com.auth0.android.jwt.JWT
import com.harper.carnet.data.storage.AppStorage
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class JwtTokenInterceptor(private val storage: AppStorage) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        storage.getJwtToken().also { jwt ->
            if (!JwtTokenHandler.isExpired(jwt)) {
                requestBuilder.addHeader(HEADER_AUTHORIZATION, jwt)
            }
        }

        return chain.proceed(requestBuilder.build())
    }

    object JwtTokenHandler {

        fun isExpired(token: String): Boolean {
            val jwt = kotlin.runCatching {
                JWT(token)
            }.getOrNull()

            return if (jwt == null) {
                false
            } else {
                val exp = getClaim(jwt, "exp")?.asDate()
                if (exp == null) {
                    true
                } else exp >= Date()
            }
        }

        @Throws(IllegalStateException::class, IllegalArgumentException::class)
        private fun getClaim(jwt: JWT, claim: String): Claim? = jwt.claims[claim]
    }

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
    }
}