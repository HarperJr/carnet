package com.harper.carnet.domain.model

enum class NotificationType(val code: String) {
    TRAFFIC_CONGESTION("traffic"),
    DANGEROUS_MANEUVER("maneuver"),
    AMBULANCE("ambulance"),
    COLLAPSE("collapse"),
    SUPPORT("support"),
    MESSAGE("message")
}