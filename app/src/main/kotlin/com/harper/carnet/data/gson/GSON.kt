package com.harper.carnet.data.gson

import com.google.gson.GsonBuilder

object GSON {
    val gson = GsonBuilder()
        .enableComplexMapKeySerialization()
        .setPrettyPrinting()
        .serializeNulls()
        .create()

    inline fun <reified T> fromJson(json: String): T {
        return gson.fromJson(json, T::class.java)
    }

    inline fun <reified T> toJson(entity: T): String {
        return gson.toJson(entity)
    }
}