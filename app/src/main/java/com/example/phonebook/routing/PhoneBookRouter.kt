package com.example.phonebook.routing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class Screen {
    object PhoneBooks: Screen()
    object EditDetail: Screen()
    object SeeDetail: Screen()
    object Trash: Screen()
}

object PhoneBooksRouter {
    var currentScreen: Screen by mutableStateOf(Screen.PhoneBooks)

    fun navigateTo(destination: Screen) {
        currentScreen = destination
    }
}