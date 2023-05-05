package com.example.phonebook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhoneBookDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "company") val company: String,
    @ColumnInfo(name = "phone") val phone: String,
    @ColumnInfo(name = "phone_id") val phoneId: Long,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "email_id") val emailId: Long,
    @ColumnInfo(name = "profile_id") val profile: Long,
    @ColumnInfo(name = "in_trash") val isInTrash: Boolean
){
    companion object{
        val DEFAULT_BOOKS = listOf(
            PhoneBookDbModel(1, "Police", "", "Police", "191", 4, "", 4, 1, false),
            PhoneBookDbModel(2, "Ambulance", "", "Hospital", "1669", 4, "", 4, 1, false),
        )
        val DEFAULT_BOOK = DEFAULT_BOOKS[0]
    }
}
