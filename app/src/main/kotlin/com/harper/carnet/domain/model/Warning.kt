package com.harper.carnet.domain.model

import java.util.*

class Warning(val type: WarningType, val diagnosticValue: DiagnosticValue<*>, val time: Date)