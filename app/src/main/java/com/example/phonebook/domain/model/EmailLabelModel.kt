package com.example.phonebook.domain.model

import com.example.phonebook.database.EmailLabel

data class EmailLabelModel(
    val id: Long,
    val name: String,
) {
    companion object {
        val DEFAULT = with(EmailLabel.DEFAULT_EMAIL) {  EmailLabelModel(id, name,) }
    }
}