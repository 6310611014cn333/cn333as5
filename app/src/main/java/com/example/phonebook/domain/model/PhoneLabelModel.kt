package com.example.phonebook.domain.model

import com.example.phonebook.database.EmailLabel
import com.example.phonebook.database.PhoneLabel

data class PhoneLabelModel (
    val id: Long,
    val name: String,
) {
    companion object {
        val DEFAULT = with(PhoneLabel.DEFAULT_PHONE) {  PhoneLabelModel(id, name,) }
    }
}