package com.example.phonebook.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [
    PhoneBookDbModel::class, PhoneLabel::class,
    EmailLabel::class, ProfileDbModel::class],
    version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun phoneBookDao(): PhoneBookDao
    abstract fun phoneLabelDao(): PhoneLabelDao
    abstract fun emailDao(): EmailDao
    abstract fun profileDao(): ProfileDao

    companion object {
        private const val DATABASE_NAME = "phone-book-database"
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            var instance = INSTANCE
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()

                INSTANCE = instance
            }

            return instance
        }
    }
}