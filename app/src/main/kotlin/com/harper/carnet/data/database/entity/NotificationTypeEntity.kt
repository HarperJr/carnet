package com.harper.carnet.data.database.entity

enum class NotificationTypeEntity {
    TRAFFIC_CONGESTION,
    MESSAGE,
    DANGER;

    companion object {

        fun of(ordinal: Int) = values().find { it.ordinal == ordinal }
            ?: throw IllegalArgumentException("Unable to find type by ordinal=$ordinal")
    }
}