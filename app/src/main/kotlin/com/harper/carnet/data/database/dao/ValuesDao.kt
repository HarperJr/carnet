package com.harper.carnet.data.database.dao

import androidx.room.Dao
import com.harper.carnet.data.database.dao.base.BaseDao
import com.harper.carnet.data.database.entity.ValueEntity

@Dao
interface ValuesDao : BaseDao<ValueEntity>