package com.harper.carnet.di

import com.harper.carnet.ui.car.CarFragment
import com.harper.carnet.ui.car.CarViewModel
import com.harper.carnet.ui.map.MapFragment
import com.harper.carnet.ui.map.MapViewModel
import com.harper.carnet.ui.session.SessionsFragment
import com.harper.carnet.ui.session.SessionsViewModel
import com.harper.carnet.ui.session.create.SessionCreateFragment
import com.harper.carnet.ui.session.create.SessionCreateViewModel
import com.harper.carnet.ui.settings.regions.RegionsFragment
import com.harper.carnet.ui.settings.regions.RegionsViewModel
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
                    SessionsViewModel(get())
                }
            }

            scope<RegionsFragment> {
                viewModel {
                    RegionsViewModel(get())
                }
            }

            scope<SessionCreateFragment> {
                viewModel {
                    SessionCreateViewModel(get(), get())
                }
            }
        }
    }
}