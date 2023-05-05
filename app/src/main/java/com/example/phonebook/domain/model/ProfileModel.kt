package com.example.phonebook.domain.model

import com.example.phonebook.database.ProfileDbModel

data class ProfileModel (
    val id: Long,
    val name: String,
    val hex: String
) {
    companion object {
        val DEFAULT = with(ProfileDbModel.DEFAULT_COLOR) { ProfileModel(id, name, hex) }
    }
}