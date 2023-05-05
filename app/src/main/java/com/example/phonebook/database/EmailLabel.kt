package com.example.phonebook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EmailLabel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String
){
    companion object {
        val DEFAULT_EMAILS = listOf(
            EmailLabel(1, "Private"),
            EmailLabel(2, "Home"),
            EmailLabel(3, "Work"),
            EmailLabel(4, "Emergency"),
            EmailLabel(5, "Other"),
        )
        val DEFAULT_EMAIL = DEFAULT_EMAILS[0]
    }
}
