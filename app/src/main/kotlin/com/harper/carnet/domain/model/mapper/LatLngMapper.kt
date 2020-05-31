package com.harper.carnet.domain.model.mapper

import com.harper.carnet.base.BaseMapper
import com.harper.carnet.base.Mapper
import com.harper.carnet.data.database.entity.LatLngEntity
import com.harper.carnet.domain.model.LatLng

/**
 * Created by HarperJr on 1:14
 **/
class LatLngMapper : BaseMapper<LatLngEntity, LatLng>() {

    override fun entityToModel(entity: LatLngEntity): LatLng {
        return LatLng(entity.lat, entity.lng)
    }

    override fun modelToEntity(model: LatLng): LatLngEntity {
        return LatLngEntity(model.lat, model.lng)
    }

    companion object : Mapper<LatLngEntity, LatLng> by LatLngMapper()
}