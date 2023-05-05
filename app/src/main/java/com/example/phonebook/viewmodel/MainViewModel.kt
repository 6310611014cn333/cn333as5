package com.example.phonebook.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonebook.database.AppDatabase
import com.example.phonebook.database.DbMapper
import com.example.phonebook.database.Repository
import com.example.phonebook.domain.model.EmailLabelModel
import com.example.phonebook.domain.model.PhoneBookModel
import com.example.phonebook.domain.model.PhoneLabelModel
import com.example.phonebook.domain.model.ProfileModel
import com.example.phonebook.routing.PhoneBooksRouter
import com.example.phonebook.routing.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : ViewModel() {
    val phoneBooksNotInTrash: LiveData<List<PhoneBookModel>> by lazy {
        repository.getAllPhoneBooksNotInTrash()
    }

    private var _phoneBookEntry = MutableLiveData(PhoneBookModel())

    val phoneBookEntry: LiveData<PhoneBookModel> = _phoneBookEntry

    val profiles: LiveData<List<ProfileModel>> by lazy {
        repository.getAllProfiles()
    }

    val phoneLabels: LiveData<List<PhoneLabelModel>> by lazy {
        repository.getAllPhoneLabels()
    }

    val emailLabels: LiveData<List<EmailLabelModel>> by lazy {
        repository.getAllEmailLabels()
    }

    val phoneBooksInTrash by lazy { repository.getAllPhoneBooksInTrash() }

    private var _selectedPhoneBooks = MutableLiveData<List<PhoneBookModel>>(listOf())

    val selectedPhoneBooks: LiveData<List<PhoneBookModel>> = _selectedPhoneBooks

    private val repository: Repository

    init {
        val db = AppDatabase.getInstance(application)
        repository = Repository(db.phoneBookDao(), db.phoneLabelDao(), db.emailDao(), db.profileDao(), DbMapper())
    }

    fun onCreateNewPhoneBookClick() {
        _phoneBookEntry.value = PhoneBookModel()
        PhoneBooksRouter.navigateTo(Screen.EditDetail)
    }

    fun onPhoneBookClick(phoneBook: PhoneBookModel) {
        _phoneBookEntry.value = phoneBook
        PhoneBooksRouter.navigateTo(Screen.SeeDetail)
    }

    fun onPhoneBookCheckedChange(phoneBook: PhoneBookModel) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.insertPhoneBook(phoneBook)
        }
    }

    fun onPhoneBookSelected(phoneBook: PhoneBookModel) {
        _selectedPhoneBooks.value = _selectedPhoneBooks.value!!.toMutableList().apply {
            if (contains(phoneBook)) {
                remove(phoneBook)
            } else {
                add(phoneBook)
            }
        }
    }

    fun restorePhoneBooks(phoneBooks: List<PhoneBookModel>) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.restorePhoneBooksFromTrash(phoneBooks.map { it.id })
            withContext(Dispatchers.Main) {
                _selectedPhoneBooks.value = listOf()
            }
        }
    }

    fun permanentlyDeletePhoneBooks(phoneBooks: List<PhoneBookModel>) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.deletePhoneBooks(phoneBooks.map { it.id })
            withContext(Dispatchers.Main) {
                _selectedPhoneBooks.value = listOf()
            }
        }
    }

    fun onPhoneBookEntryChange(phoneBook: PhoneBookModel) {
        _phoneBookEntry.value = phoneBook
    }

    fun savePhoneBook(phoneBook: PhoneBookModel) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.insertPhoneBook(phoneBook)

            withContext(Dispatchers.Main) {
                PhoneBooksRouter.navigateTo(Screen.PhoneBooks)

                _phoneBookEntry.value = PhoneBookModel()
            }
        }
    }

    fun movePhoneBookToTrash(note: PhoneBookModel) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.movePhoneBookToTrash(note.id)

            withContext(Dispatchers.Main) {
                PhoneBooksRouter.navigateTo(Screen.PhoneBooks)
            }
        }
    }
}