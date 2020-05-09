package com.harper.carnet.base

abstract class Mapper<E, M> {
    abstract fun entityToModel(entity: E): M

    abstract fun modelToEntity(model: M): E

    fun entitiesToModels(entities: List<E>): List<M> = entities.map { entityToModel(it) }

    fun modelsToEntities(models: List<M>): List<E> = models.map { modelToEntity(it) }
}