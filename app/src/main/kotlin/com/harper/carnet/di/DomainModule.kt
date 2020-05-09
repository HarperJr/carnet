package com.harper.carnet.di

import com.harper.carnet.domain.car.ValuesProvider
import com.harper.carnet.domain.car.WarningsProvider
import org.koin.core.module.Module
import org.koin.dsl.module

object DomainModule {
    operator fun invoke(): Module {
        return module {
            factory { WarningsProvider() }

            factory { ValuesProvider() }
        }
    }
}