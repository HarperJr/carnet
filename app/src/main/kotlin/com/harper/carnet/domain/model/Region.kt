package com.harper.carnet.domain.model

import java.io.Serializable

class Region(
    val id: Long,
    val code: String,
    val name: String,
    val order: Long,
    val size: Int,
    val north: Double,
    val south: Double,
    val east: Double,
    val west: Double
) : Serializable