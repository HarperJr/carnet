package com.harper.carnet.di

import com.harper.carnet.domain.diagnostics.ConnectionProvider
import com.harper.carnet.domain.diagnostics.ValuesProvider
import com.harper.carnet.domain.diagnostics.WarningsProvider
import com.harper.carnet.domain.map.location.LocationProvider
import com.harper.carnet.domain.map.regions.RegionsProvider
import com.harper.carnet.domain.session.SessionManager
import com.harper.carnet.domain.session.SessionProvider
import com.harper.carnet.domain.telematics.TelematicsProvider
import org.koin.core.module.Module
import org.koin.dsl.module

object DomainModule {

    operator fun invoke(): Module {
        return module {
            factory {
                WarningsProvider()
            }

            factory {
                ValuesProvider()
            }

            factory {
                ConnectionProvider(get())
            }

            factory {
                RegionsProvider()
            }

            factory {
                LocationProvider(get())
            }

            factory {
                SessionManager(get())
            }

            factory {
                SessionProvider(get())
            }

            factory {
                TelematicsProvider(get(), get())
            }
        }
    }
}