package com.example.phonebook.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PhoneLabelDao {
    @Query("SELECT * FROM PhoneLabel")
    fun getAll(): LiveData<List<PhoneLabel>>

    @Query("SELECT * FROM PhoneLabel")
    fun getAllSync(): List<PhoneLabel>

    @Insert
    fun insertAll(vararg phoneLabels: PhoneLabel)
}