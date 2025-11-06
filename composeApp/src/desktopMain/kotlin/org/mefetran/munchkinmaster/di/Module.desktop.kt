package org.mefetran.munchkinmaster.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.module.Module
import org.koin.dsl.module
import org.mefetran.munchkinmaster.app.createDataStore
import org.mefetran.munchkinmaster.app.getDesktopDataStorePath
import org.mefetran.munchkinmaster.data.db.AppDatabase
import org.mefetran.munchkinmaster.data.db.getAppDatabase
import org.mefetran.munchkinmaster.getDatabaseBuilder

actual fun platformModule(): Module = module {
    single<AppDatabase> {
        val builder = getDatabaseBuilder()
        getAppDatabase(builder)
    }

    single<DataStore<Preferences>> { createDataStore(
        producePath = { getDesktopDataStorePath("MunchkinMaster") }
    ) }
}