package org.mefetran.munchkinmaster

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.mefetran.munchkinmaster.di.initKoin

class MunchkinApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin(appDeclaration = {
            androidContext(this@MunchkinApplication)
            androidLogger()
        })
    }
}