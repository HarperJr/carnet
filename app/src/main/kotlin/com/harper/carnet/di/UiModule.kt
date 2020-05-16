package com.harper.carnet.di

import com.harper.carnet.ui.car.CarFragment
import com.harper.carnet.ui.car.CarViewModel
import com.harper.carnet.ui.map.MapFragment
import com.harper.carnet.ui.map.MapViewModel
import com.harper.carnet.ui.session.SessionsFragment
import com.harper.carnet.ui.session.SessionsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object UiModule {
    operator fun invoke(): Module {
        return module {
            scope<CarFragment> {
                viewModel {
                    CarViewModel(get(), get())
                }
            }

            scope<MapFragment> {
                viewModel {
                    MapViewModel(get())
                }
            }

            scope<SessionsFragment> {
                viewModel {
                    SessionsViewModel()
                }
            }
        }
    }
}