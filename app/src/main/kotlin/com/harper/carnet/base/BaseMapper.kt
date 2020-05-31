package com.harper.carnet.base

interface Mapper<E, M> {

    fun entityToModel(entity: E): M

    fun modelToEntity(model: M): E

    fun entitiesToModels(entities: List<E>): List<M>

    fun modelsToEntities(models: List<M>): List<E>
}

abstract class BaseMapper<E, M> : Mapper<E, M> {

    abstract override fun entityToModel(entity: E): M

    abstract override fun modelToEntity(model: M): E

    override fun entitiesToModels(entities: List<E>): List<M> = entities.map { entityToModel(it) }

    override fun modelsToEntities(models: List<M>): List<E> = models.map { modelToEntity(it) }
}