package com.harper.carnet.di

import com.harper.carnet.ui.car.CarFragment
import com.harper.carnet.ui.car.CarViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object UiModule {
    operator fun invoke(): Module {
        return module {
            scope<CarFragment> {
                viewModel { CarViewModel(get(), get()) }
            }
        }
    }
}