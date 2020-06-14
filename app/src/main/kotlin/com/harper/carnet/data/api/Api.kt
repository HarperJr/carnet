package com.harper.carnet.data.api

import com.harper.carnet.data.api.entity.body.*
import com.harper.carnet.data.api.entity.response.JwtTokenRes
import com.harper.carnet.data.api.entity.response.TelematicsRes
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by HarperJr on 17:36
 **/
interface Api {

    @POST("reg_device")
    fun regDevice(@Body authDeviceBody: RegDeviceBody): Single<Response<JwtTokenRes>>

    @POST("auth_device")
    fun authDevice(@Body authDeviceBody: AuthDeviceBody): Single<Response<JwtTokenRes>>

    @POST("publish_notification")
    fun publishNotification(@Body notificationBody: NotificationBody): Single<Response<Unit>>

    @POST("subscribe_notifications")
    fun subscribeNotifications(@Body pushTokenBody: PushTokenBody): Single<Response<Unit>>

    @POST("send_telematics")
    fun sendTelematics(@Body telematicsBody: TelematicsBody): Single<Response<Unit>>

    @GET("get_telematics")
    fun getTelematics(): Single<Response<List<TelematicsRes>>>
}