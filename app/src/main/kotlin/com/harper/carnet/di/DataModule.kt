package com.harper.carnet.di

import com.harper.carnet.data.storage.AppStorage
import com.harper.carnet.data.storage.SharedStorage
import org.koin.core.module.Module
import org.koin.dsl.module

object DataModule {
    operator fun invoke(): Module {
        return module {
            single {
                SharedStorage(get())
            }

            factory {
                AppStorage(get())
            }
        }
    }
}