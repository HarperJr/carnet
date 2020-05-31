package com.harper.carnet.data.repository

import androidx.sqlite.db.SupportSQLiteQueryBuilder
import com.harper.carnet.base.Mapper
import com.harper.carnet.data.database.Table
import com.harper.carnet.data.database.dao.base.BaseDao
import com.harper.carnet.data.database.entity.base.BaseEntity

abstract class BaseRepository<E : BaseEntity, M>(private val dao: BaseDao<E>, private val baseMapper: Mapper<E, M>) :
    Repository<M> {
    private val tableName: String
        get() = Table.fromDao(dao::class).tableName

    override fun find(id: Int): M {
        return baseMapper.entityToModel(
            dao.find(
                SupportSQLiteQueryBuilder.builder(tableName).selection("id", arrayOf(id)).create()
            )
        )
    }

    override fun insert(model: M) {
        dao.insert(baseMapper.modelToEntity(model))
    }

    override fun insert(models: List<M>) {
        dao.insert(baseMapper.modelsToEntities(models))
    }

    override fun delete(id: Int) {
        dao.delete(SupportSQLiteQueryBuilder.builder(tableName).selection("id", arrayOf(id)).create())
    }

    override fun delete(model: M) {
        dao.delete(baseMapper.modelToEntity(model))
    }

    override fun update(model: M) {
        dao.update(baseMapper.modelToEntity(model))
    }

    override fun drop() {
        dao.delete(SupportSQLiteQueryBuilder.builder(tableName).create())
    }
}