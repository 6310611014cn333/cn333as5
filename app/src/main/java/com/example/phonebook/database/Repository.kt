package com.example.phonebook.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.phonebook.domain.model.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Repository (
    private  val phoneBookDao: PhoneBookDao,
    private  val phoneLabelDao: PhoneLabelDao,
    private  val emailDao: EmailDao,
    private  val profileDao: ProfileDao,
    private val dbMapper: DbMapper
    ) {

        // Working Phone
        private val phoneBooksNotInTrashLiveData: MutableLiveData<List<PhoneBookModel>> by lazy {
            MutableLiveData<List<PhoneBookModel>>()
        }

        fun getAllPhoneBooksNotInTrash(): LiveData<List<PhoneBookModel>> = phoneBooksNotInTrashLiveData

        // Deleted Notes
        private val phoneBooksInTrashLiveData: MutableLiveData<List<PhoneBookModel>> by lazy {
            MutableLiveData<List<PhoneBookModel>>()
        }

        fun getAllPhoneBooksInTrash(): LiveData<List<PhoneBookModel>> = phoneBooksInTrashLiveData

        init {
            initDatabase(this::updatePhoneBooksLiveData)
        }

        /**
         * Populates database with colors if it is empty.
         */
        private fun initDatabase(postInitAction: () -> Unit) {
            GlobalScope.launch {
                // Prepopulate profiles
                val profiles = ProfileDbModel.DEFAULT_COLORS.toTypedArray()
                val dbProfiles = profileDao.getAllSync()
                if (dbProfiles.isNullOrEmpty()) {
                    profileDao.insertAll(*profiles)
                }

                // Prepopulate phones
                val phoneBooks = PhoneBookDbModel.DEFAULT_BOOKS.toTypedArray()
                val dbPhoneBooks = phoneBookDao.getAllSync()
                if (dbPhoneBooks.isNullOrEmpty()) {
                    phoneBookDao.insertAll(*phoneBooks)
                }

                // Prepopulate phone labels
                val phoneLabels = PhoneLabel.DEFAULT_PHONES.toTypedArray()
                val dbPhoneLabels = phoneLabelDao.getAllSync()
                if (dbPhoneLabels.isNullOrEmpty()) {
                    phoneLabelDao.insertAll(*phoneLabels)
                }

                // Prepopulate email labels
                val emailLabels = EmailLabel.DEFAULT_EMAILS.toTypedArray()
                val dbEmailLabels = emailDao.getAllSync()
                if (dbEmailLabels.isNullOrEmpty()) {
                    emailDao.insertAll(*emailLabels)
                }

                postInitAction.invoke()
            }
        }

        // get list of working notes or deleted notes
        private fun getAllPhoneBooksDependingOnTrashStateSync(inTrash: Boolean): List<PhoneBookModel> {
            val profileDbModels: Map<Long, ProfileDbModel> = profileDao.getAllSync().map { it.id to it }.toMap()
            val phoneLabels: Map<Long, PhoneLabel> = phoneLabelDao.getAllSync().map { it.id to it }.toMap()
            val emailLabels: Map<Long, EmailLabel> = emailDao.getAllSync().map { it.id to it }.toMap()
            val dbNotes: List<PhoneBookDbModel> = phoneBookDao.getAllSync().filter { it.isInTrash == inTrash }
            return dbMapper.mapPhoneBooks(dbNotes, profileDbModels, phoneLabels, emailLabels)
        }

        fun insertPhoneBook(phoneBook: PhoneBookModel) {
            phoneBookDao.insert(dbMapper.mapDbPhoneBook(phoneBook))
            updatePhoneBooksLiveData()
        }

        fun deletePhoneBooks(phoneBookIds: List<Long>) {
            phoneBookDao.delete(phoneBookIds)
            updatePhoneBooksLiveData()
        }

        fun movePhoneBookToTrash(phoneBookId: Long) {
            val dbNote = phoneBookDao.findByIdSync(phoneBookId)
            val newDbNote = dbNote.copy(isInTrash = true)
            phoneBookDao.insert(newDbNote)
            updatePhoneBooksLiveData()
        }

        fun restorePhoneBooksFromTrash(phoneBookIds: List<Long>) {
            val dbPhoneBooksInTrash = phoneBookDao.getPhoneBooksByIdsSync(phoneBookIds)
            dbPhoneBooksInTrash.forEach {
                val newDbNote = it.copy(isInTrash = false)
                phoneBookDao.insert(newDbNote)
            }
            updatePhoneBooksLiveData()
        }

        fun getAllProfiles(): LiveData<List<ProfileModel>> =
            Transformations.map(profileDao.getAll()) { dbMapper.mapProfiles(it) }

        fun getAllPhoneLabels(): LiveData<List<PhoneLabelModel>> =
            Transformations.map(phoneLabelDao.getAll()) { dbMapper.mapPhoneLabels(it) }

        fun getAllEmailLabels(): LiveData<List<EmailLabelModel>> =
            Transformations.map(emailDao.getAll()) { dbMapper.mapEmailLabels(it) }

        private fun updatePhoneBooksLiveData() {
            phoneBooksNotInTrashLiveData.postValue(getAllPhoneBooksDependingOnTrashStateSync(false))
            phoneBooksInTrashLiveData.postValue(getAllPhoneBooksDependingOnTrashStateSync(true))
        }
    }