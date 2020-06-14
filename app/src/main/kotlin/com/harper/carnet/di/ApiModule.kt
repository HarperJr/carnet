package com.harper.carnet.di

import com.harper.carnet.BuildConfig
import com.harper.carnet.data.api.Api
import com.harper.carnet.data.api.ApiExecutor
import com.harper.carnet.data.api.interceptor.JwtTokenInterceptor
import com.harper.carnet.data.gson.GSON.gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by HarperJr on 1:50
 **/
object ApiModule {
    private const val CALL_TIMEOUT = 10000L

    operator fun invoke(): Module {
        return module {
            single {
                OkHttpClient().newBuilder()
                    .addInterceptor(JwtTokenInterceptor(get()))
                    .addInterceptor(HttpLoggingInterceptor()
                        .also { it.level = HttpLoggingInterceptor.Level.BODY })
                    .callTimeout(CALL_TIMEOUT, TimeUnit.MILLISECONDS)
                    .build()
            }

            single {
                Retrofit.Builder()
                    .client(get())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BuildConfig.API_URL)
                    .build()
            }

            single {
                ApiExecutor(get<Retrofit>().create(Api::class.java))
            }
        }
    }
}