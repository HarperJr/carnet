package com.harper.carnet.data.api.entity.body

import java.sql.Timestamp

class TelematicsBody(val timestamp: Long, val latitude: Double, val longitude: Double, val speed: Double, val rotation: Double)