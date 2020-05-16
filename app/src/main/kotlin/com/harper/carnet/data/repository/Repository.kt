package com.harper.carnet.data.repository

interface Repository<T> {

    fun find(id: Int)

    fun insert(model: T)

    fun insert(models: List<T>)

    fun delete(id: Int)

    fun delete(model: T)

    fun update(model: T)

    fun drop()
}