package com.example.phonebook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhoneLabel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String
){
    companion object {
        val DEFAULT_PHONES = listOf(
            PhoneLabel(1, "Mobile"),
            PhoneLabel(2, "Home"),
            PhoneLabel(3, "Work"),
            PhoneLabel(4, "Emergency"),
            PhoneLabel(5, "Other"),
        )
        val DEFAULT_PHONE = DEFAULT_PHONES[0]
    }
}
