package com.harper.carnet.data.api

import com.harper.carnet.data.api.entity.body.DeviceBody
import com.harper.carnet.data.api.entity.body.NotificationBody
import com.harper.carnet.data.api.entity.body.PushTokenBody
import com.harper.carnet.data.api.entity.body.TelematicsBody
import com.harper.carnet.data.api.entity.response.ErrorRes
import com.harper.carnet.data.gson.GSON.gson
import com.harper.carnet.domain.model.Telematics
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response

/**
 * Created by HarperJr on 11:19
 **/
interface ApiService {

    fun regDevice(identity: String): Single<String>

    fun authDevice(identity: String): Single<String>

    fun publishNotification(message: String, type: String, lat: Double, lng: Double): Completable

    fun subscribeNotifications(token: String): Completable

    fun sendTelematics(lat: Double, lng: Double, speed: Double, rot: Double): Completable

    fun getTelematics(): Single<List<Telematics>>
}

class ApiExecutor(private val api: Api) : ApiService {

    override fun regDevice(identity: String): Single<String> {
        return execute { regDevice(DeviceBody(identity)) }
            .map { it.token }
    }

    override fun authDevice(identity: String): Single<String> {
        return execute { authDevice(DeviceBody(identity)) }
            .map { it.token }
    }

    override fun publishNotification(message: String, type: String, lat: Double, lng: Double): Completable {
        return execute { publishNotification(NotificationBody(message, type, lat, lng)) }
            .ignoreElement()
    }

    override fun subscribeNotifications(token: String): Completable {
        return execute { subscribeNotifications(PushTokenBody(token)) }
            .ignoreElement()
    }

    override fun sendTelematics(lat: Double, lng: Double, speed: Double, rot: Double): Completable {
        return execute { sendTelematics(TelematicsBody(lat, lng, speed, rot)) }
            .ignoreElement()
    }

    override fun getTelematics(): Single<List<Telematics>> {
        return execute { getTelematics() }
            .map { telematics ->
                telematics.map {
                    Telematics(it.timestamp, it.latitude, it.longitude, it.speed, it.rotation)
                }
            }
    }

    /**
     * This method consumes any api method invocation and returns it's body mapped to response class
     *
     * @param serviceInvocation invocation of api service, throws retrofit response with model
     * @exception ApiException if response code is 4xx, 5xx
     */
    private fun <E> execute(serviceInvocation: Api.() -> Single<Response<E>>): Single<E> {
        return serviceInvocation.invoke(api)
            .map { response ->
                if (response.isSuccessful) {
                    response.body() ?: throw ApiException(response.code(), "Response is null")
                } else {
                    response.errorBody()?.let { errorBody ->
                        val body: ErrorRes? = gson.fromJson(errorBody.string(), ErrorRes::class.java)
                        throw ApiException(response.code(), body?.message ?: UNDEFINED_ERROR)
                    } ?: throw ApiException(response.code(), response.message())
                }
            }
    }

    companion object {
        private const val UNDEFINED_ERROR = "Something is gone wrong, please retry"
    }
}