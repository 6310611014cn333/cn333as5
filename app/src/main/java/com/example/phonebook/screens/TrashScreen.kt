package com.example.phonebook.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import com.example.phonebook.domain.model.PhoneBookModel
import com.example.phonebook.routing.Screen
import com.example.phonebook.ui.components.AppDrawer
import com.example.phonebook.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import com.example.phonebook.R.drawable
import com.example.phonebook.ui.components.PhoneBook

private const val NO_DIALOG = 1
private const val RESTORE_PHONEBOOKS_DIALOG = 2
private const val PERMANENTLY_DELETE_DIALOG = 3

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterialApi
fun TrashScreen(viewModel: MainViewModel) {

    val phoneBooksInThrash: List<PhoneBookModel> by viewModel.phoneBooksInTrash
        .observeAsState(listOf())

    val selectedPhoneBooks: List<PhoneBookModel> by viewModel.selectedPhoneBooks
        .observeAsState(listOf())

    val dialogState = rememberSaveable { mutableStateOf(NO_DIALOG) }

    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            val areActionsVisible = selectedPhoneBooks.isNotEmpty()
            TrashTopAppBar(
                onNavigationIconClick = {
                    coroutineScope.launch { scaffoldState.drawerState.open() }
                },
                onRestorePhoneBooksClick = { dialogState.value = RESTORE_PHONEBOOKS_DIALOG },
                onDeletePhoneBooksClick = { dialogState.value = PERMANENTLY_DELETE_DIALOG },
                areActionsVisible = areActionsVisible
            )
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            AppDrawer(
                currentScreen = Screen.Trash,
                closeDrawerAction = {
                    coroutineScope.launch { scaffoldState.drawerState.close() }
                }
            )
        },
        content = {
            Content(
                phoneBooks = phoneBooksInThrash,
                onPhoneBookClick = { viewModel.onPhoneBookSelected(it) },
                selectedPhoneBooks = selectedPhoneBooks
            )

            val dialog = dialogState.value
            if (dialog != NO_DIALOG) {
                val confirmAction: () -> Unit = when (dialog) {
                    RESTORE_PHONEBOOKS_DIALOG -> {
                        {
                            viewModel.restorePhoneBooks(selectedPhoneBooks)
                            dialogState.value = NO_DIALOG
                        }
                    }
                    PERMANENTLY_DELETE_DIALOG -> {
                        {
                            viewModel.permanentlyDeletePhoneBooks(selectedPhoneBooks)
                            dialogState.value = NO_DIALOG
                        }
                    }
                    else -> {
                        {
                            dialogState.value = NO_DIALOG
                        }
                    }
                }

                AlertDialog(
                    onDismissRequest = { dialogState.value = NO_DIALOG },
                    title = { Text(mapDialogTitle(dialog)) },
                    text = { Text(mapDialogText(dialog)) },
                    confirmButton = {
                        TextButton(onClick = confirmAction) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { dialogState.value = NO_DIALOG }) {
                            Text("Dismiss")
                        }
                    }
                )
            }
        }
    )
}

@Composable
private fun TrashTopAppBar(
    onNavigationIconClick: () -> Unit,
    onRestorePhoneBooksClick: () -> Unit,
    onDeletePhoneBooksClick: () -> Unit,
    areActionsVisible: Boolean
) {
    TopAppBar(
        title = { Text(text = "Trash", color = MaterialTheme.colors.onPrimary) },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Filled.List,
                    contentDescription = "Drawer Button"
                )
            }
        },
        actions = {
            if (areActionsVisible) {
                IconButton(onClick = onRestorePhoneBooksClick) {
                    Icon(
                        painter = painterResource(id = drawable.baseline_restore_from_trash_24),
                        contentDescription = "Restore Contact Button",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                IconButton(onClick = onDeletePhoneBooksClick) {
                    Icon(
                        painter = painterResource(id = drawable.baseline_delete_forever_24),
                        contentDescription = "Delete Contact Button",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    )
}

@Composable
@ExperimentalMaterialApi
private fun Content(
    phoneBooks: List<PhoneBookModel>,
    onPhoneBookClick: (PhoneBookModel) -> Unit,
    selectedPhoneBooks: List<PhoneBookModel>,
) {

    Column {

        LazyColumn {
            items(count = phoneBooks.size) { phoneBookIndex ->
                val phoneBook = phoneBooks[phoneBookIndex]
                val isPhoneBookSelected = selectedPhoneBooks.contains(phoneBook)
                PhoneBook(
                    phoneBook = phoneBook,
                    onPhoneBookClick = onPhoneBookClick,
                    isSelected = isPhoneBookSelected
                )
            }
        }
    }
}

private fun mapDialogTitle(dialog: Int): String = when (dialog) {
    RESTORE_PHONEBOOKS_DIALOG -> "Restore contacts"
    PERMANENTLY_DELETE_DIALOG -> "Delete contacts forever"
    else -> throw RuntimeException("Dialog not supported: $dialog")
}

private fun mapDialogText(dialog: Int): String = when (dialog) {
    RESTORE_PHONEBOOKS_DIALOG -> "Are you sure you want to restore selected contacts?"
    PERMANENTLY_DELETE_DIALOG -> "Are you sure you want to delete selected contacts permanently?"
    else -> throw RuntimeException("Dialog not supported: $dialog")
}