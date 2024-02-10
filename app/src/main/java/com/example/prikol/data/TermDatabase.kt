package com.example.prikol.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

val databaseName = "prikol_database" // The name of the database file

@Database(entities = [Term::class], version = 1, exportSchema = false)
abstract class TermDatabase : RoomDatabase() {
    abstract fun termDao(): TermDao

    companion object {
        @Volatile
        private var Instance: TermDatabase? = null
        fun getDatabase(context: Context): TermDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TermDatabase::class.java, databaseName).setJournalMode(JournalMode.TRUNCATE)
                    .fallbackToDestructiveMigration().build().also {Instance = it}
            }
        }
    }
}