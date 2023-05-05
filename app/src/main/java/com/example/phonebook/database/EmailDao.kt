package com.example.phonebook.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EmailDao {
    @Query("SELECT * FROM EmailLabel")
    fun getAll(): LiveData<List<EmailLabel>>

    @Query("SELECT * FROM EmailLabel")
    fun getAllSync(): List<EmailLabel>

    @Insert
    fun insertAll(vararg emailLabels: EmailLabel)
}