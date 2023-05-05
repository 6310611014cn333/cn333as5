package com.example.phonebook.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProfileDao {
    @Query("SELECT * FROM ProfileDBModel")
    fun getAll(): LiveData<List<ProfileDbModel>>

    @Query("SELECT * FROM ProfileDBModel")
    fun getAllSync(): List<ProfileDbModel>

    @Insert
    fun insertAll(vararg profileDBModels: ProfileDbModel)
}