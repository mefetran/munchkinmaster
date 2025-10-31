package org.mefetran.munchkinmaster

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.mefetran.munchkinmaster.data.db.AppDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("app_database.db")

    return Room.databaseBuilder(
        context = appContext,
        name = dbFile.absolutePath,
    )
}