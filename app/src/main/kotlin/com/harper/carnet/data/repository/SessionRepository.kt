package com.harper.carnet.data.repository

import com.harper.carnet.data.database.dao.SessionsDao
import com.harper.carnet.data.database.entity.SessionEntity
import com.harper.carnet.domain.model.Session
import com.harper.carnet.domain.model.mapper.SessionMapper

class SessionRepository(protected val sessionsDao: SessionsDao) : BaseRepository<SessionEntity, Session>(sessionsDao, SessionMapper) {

    fun findActiveSession(): Session? {
        return sessionsDao.findActiveSession()?.let { SessionMapper.entityToModel(it) }
    }
}