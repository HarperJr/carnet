package com.harper.carnet.ui.support.perms

import android.Manifest

enum class Permission(val manifestString: String) {
    COARSE_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION),
    FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION);

    companion object {
        fun of(manifestString: String) = values().find { it.manifestString == manifestString }
            ?: throw IllegalArgumentException("Unable to find permission with manifestString=$manifestString")
    }
}
