package com.harper.carnet.di

import androidx.room.Room
import com.harper.carnet.data.database.Database
import com.harper.carnet.data.repository.SessionRepository
import com.harper.carnet.data.storage.AppStorage
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
                Room.databaseBuilder(get(), Database::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
            }

            factory { get<Database>().regionsDao() }

            factory { get<Database>().sessionsDao() }

            factory { SessionRepository(get()) }
        }
    }
}