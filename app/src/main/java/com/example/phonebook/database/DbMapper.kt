package com.example.phonebook.database

import com.example.phonebook.domain.model.*

class DbMapper {
    // Create list of PhoneBookModels
    fun mapPhoneBooks(
        phoneBookDbModels: List<PhoneBookDbModel>,
        profileDbModels: Map<Long, ProfileDbModel>,
        phoneLabelModels: Map<Long, PhoneLabel>,
        emailLabelModels: Map<Long, EmailLabel>,
    ): List<PhoneBookModel> = phoneBookDbModels.map {
        val profileDbModel = profileDbModels[it.profile]
            ?: throw RuntimeException("Profile for color: ${it.profile} was not found. Make sure that all colors are passed to this method")
        val phoneLabelsModel = phoneLabelModels[it.phoneId]
            ?: throw RuntimeException("Phone label for id: ${it.profile} was not found. Make sure that all colors are passed to this method")
        val emailLabelsModel = emailLabelModels[it.emailId]
            ?: throw RuntimeException("Email label for id: ${it.profile} was not found. Make sure that all colors are passed to this method")

        mapPhoneBook(it, profileDbModel, phoneLabelsModel, emailLabelsModel)
    }

    // convert PhoneBookDbModel to PhoneBookModel
    fun mapPhoneBook(
        phoneBookDbModel: PhoneBookDbModel,
        profileDbModel: ProfileDbModel,
        phoneLabelModel: PhoneLabel,
        emailLabelModel: EmailLabel
    ): PhoneBookModel {
        val profile = mapProfile(profileDbModel)
        val phoneLabel = mapPhoneLabel(phoneLabelModel)
        val emailLabel = mapEmailLabel(emailLabelModel)
        return with(phoneBookDbModel) { PhoneBookModel(
            id, firstName, lastName, company, phone, phoneLabel,
            email, emailLabel, profile
        ) }
    }

    // convert list of ProfileDdModels to list of ProfileModels
    fun mapProfiles(profileDbModels: List<ProfileDbModel>): List<ProfileModel> =
        profileDbModels.map { mapProfile(it) }

    // convert ProfileDbModel to ProfileModel
    fun mapProfile(profileDbModel: ProfileDbModel): ProfileModel =
        with(profileDbModel) { ProfileModel(id, name, hex) }

    // convert list of PhoneLabels to list of PhoneLabelModels
    fun mapPhoneLabels(phoneLabelModels: List<PhoneLabel>): List<PhoneLabelModel> =
        phoneLabelModels.map { mapPhoneLabel(it) }

    // convert PhoneLabel to PhoneLabelModel
    fun mapPhoneLabel(phoneLabelModel: PhoneLabel): PhoneLabelModel =
        with(phoneLabelModel) { PhoneLabelModel(id, name) }

    // convert list of EmailLabels to list of EmailLabelModels
    fun mapEmailLabels(emailLabelModels: List<EmailLabel>): List<EmailLabelModel> =
        emailLabelModels.map { mapEmailLabel(it) }

    // convert EmailLabel to EmailLabelModel
    fun mapEmailLabel(emailLabelModel: EmailLabel): EmailLabelModel =
        with(emailLabelModel) { EmailLabelModel(id, name) }

    // convert PhoneBookModel back to PhoneBookDbModel
    fun mapDbPhoneBook(note: PhoneBookModel): PhoneBookDbModel =
        with(note) {
            if (id == NEW_BOOK_ID)
                PhoneBookDbModel(
                    firstName = firstName,
                    lastName = lastName,
                    company = company,
                    phone = phone,
                    phoneId = phoneLabel.id,
                    email = email,
                    emailId = emailLabel.id,
                    profile = profile.id,
                    isInTrash = false
                )
            else
                PhoneBookDbModel(id, firstName, lastName,
                    company, phone, phoneLabel.id,
                    email, emailLabel.id, profile.id, false)
        }
}