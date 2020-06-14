package com.harper.carnet.ui.map

import com.harper.carnet.domain.model.Telematics
import com.harper.carnet.ui.map.delegate.NavigationMapDelegate

class TelematicsDisplayManager(mapDelegate: NavigationMapDelegate) {
    private val telematicsMap: MutableMap<String, Telematics> = mutableMapOf()

    fun setTelematics(telematics: List<Telematics>) {

    }
}