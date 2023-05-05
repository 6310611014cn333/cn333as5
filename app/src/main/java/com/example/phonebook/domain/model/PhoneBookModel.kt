package com.example.phonebook.domain.model

import com.example.phonebook.database.EmailLabel
import com.example.phonebook.database.PhoneLabel

const val NEW_BOOK_ID = -1L

data class PhoneBookModel(
    val id: Long = NEW_BOOK_ID,
    val firstName: String = "",
    val lastName: String = "",
    val company: String = "",
    val phone: String = "",
    val phoneLabel: PhoneLabelModel = PhoneLabelModel.DEFAULT,
    val email: String = "",
    val emailLabel: EmailLabelModel = EmailLabelModel.DEFAULT,
    val profile: ProfileModel = ProfileModel.DEFAULT,
)