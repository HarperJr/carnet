package com.harper.carnet.domain.model.mapper

import com.harper.carnet.base.BaseMapper
import com.harper.carnet.base.Mapper
import com.harper.carnet.data.database.entity.SessionEntity
import com.harper.carnet.domain.model.Location
import com.harper.carnet.domain.model.Session

/**
 * Created by HarperJr on 0:05
 **/
class SessionMapper : BaseMapper<SessionEntity, Session>() {

    override fun entityToModel(entity: SessionEntity): Session {
        val endLocation = Location(LatLngMapper.entityToModel(entity.endLocation), entity.endLocationName)
        val startLocation = Location(LatLngMapper.entityToModel(entity.startLocation), entity.startLocationName)

        return Session(
            entity.id,
            entity.startTime,
            entity.endTime,
            entity.isActive,
            startLocation,
            endLocation,
            emptyList(),
            emptyList()
        )
    }

    override fun modelToEntity(model: Session): SessionEntity {
        return SessionEntity(
            model.startTime,
            model.endTime,
            model.isActive,
            LatLngMapper.modelToEntity(model.startLocation.latLng),
            LatLngMapper.modelToEntity(model.endLocation.latLng),
            model.startLocation.name,
            model.endLocation.name,
            emptyList()
        )
    }

    companion object : Mapper<SessionEntity, Session> by SessionMapper()
}