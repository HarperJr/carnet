package com.harper.carnet.di

import com.harper.carnet.ui.diagnostics.DiagnosticsFragment
import com.harper.carnet.ui.diagnostics.DiagnosticsViewModel
import com.harper.carnet.ui.map.MapFragment
import com.harper.carnet.ui.map.MapViewModel
import com.harper.carnet.ui.map.notification.NotificationCreateFragment
import com.harper.carnet.ui.map.notification.NotificationCreateViewModel
import com.harper.carnet.ui.session.SessionsFragment
import com.harper.carnet.ui.session.SessionsViewModel
import com.harper.carnet.ui.session.create.SessionCreateFragment
import com.harper.carnet.ui.session.create.SessionCreateViewModel
import com.harper.carnet.ui.settings.connection.ConnectionSettingsFragment
import com.harper.carnet.ui.settings.connection.ConnectionSettingsViewModel
import com.harper.carnet.ui.settings.regions.RegionsFragment
import com.harper.carnet.ui.settings.regions.RegionsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object UiModule {

    operator fun invoke(): Module {
        return module {
            scope<DiagnosticsFragment> {
                viewModel {
                    DiagnosticsViewModel(get(), get(), get())
                }
            }

            scope<MapFragment> {
                viewModel {
                    MapViewModel(get(), get(), get())
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

            scope<ConnectionSettingsFragment> {
                viewModel {
                    ConnectionSettingsViewModel(get())
                }
            }

            scope<SessionCreateFragment> {
                viewModel {
                    SessionCreateViewModel(get(), get())
                }
            }

            scope<NotificationCreateFragment> {
                viewModel {
                    NotificationCreateViewModel(get(), get())
                }
            }
        }
    }
}