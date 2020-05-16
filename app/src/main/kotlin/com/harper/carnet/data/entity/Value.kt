package com.harper.carnet.data.entity

class Value(
    override val id: Int,
    val voltage: Double,
    val speed: Int,
    val engine: Int,
    val rpm: Int,
    val fuel: Int
) : Entity()