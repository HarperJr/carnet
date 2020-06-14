package com.harper.carnet.di

import androidx.room.Room
import com.harper.carnet.data.assets.AssetsManager
import com.harper.carnet.data.database.Database
import com.harper.carnet.data.diagnostics.client.DiagnosticsClient
import com.harper.carnet.data.diagnostics.server.DiagnosticsServer
import com.harper.carnet.data.repository.SessionRepository
import com.harper.carnet.data.storage.AppStorage
import com.harper.carnet.data.storage.SettingsStorage
import com.harper.carnet.data.storage.SharedStorage
import org.koin.core.module.Module
import org.koin.dsl.module

object DataModule {
    private const val DATABASE_NAME = "local_db"

    operator fun invoke(): Module {
        return module {
            single {
                SharedStorage(get())
            }

            single {
                AppStorage(get())
            }

            single {
                SettingsStorage(get())
            }

            single {
                AssetsManager(get())
            }

            single {
                DiagnosticsServer(get())
            }

            single {
                DiagnosticsClient()
            }

            single {
                Room.databaseBuilder(get(), Database::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
            }

            factory {
                get<Database>().regionsDao()
            }

            factory {
                get<Database>().sessionsDao()
            }

            factory {
                SessionRepository(get())
            }
        }
    }
}