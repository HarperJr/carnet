package com.harper.carnet.data.database.entity

enum class SessionValueTypeEntity {
    ACCUMULATOR,
    ROTATION,
    WARNING,
    SPEED,
    FUEL;

    companion object {

        fun of(ordinal: Int) = values().find { it.ordinal == ordinal }
            ?: throw IllegalArgumentException("Unable to find type by ordinal=$ordinal")
    }
}