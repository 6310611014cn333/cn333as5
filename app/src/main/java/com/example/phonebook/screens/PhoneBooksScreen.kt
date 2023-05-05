package com.example.phonebook.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.example.phonebook.domain.model.PhoneBookModel
import com.example.phonebook.routing.Screen
import com.example.phonebook.ui.components.AppDrawer
import com.example.phonebook.ui.components.PhoneBook
import com.example.phonebook.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalMaterialApi
@Composable
fun PhoneBooksScreen(viewModel: MainViewModel) {
    val phoneBooks by viewModel.phoneBooksNotInTrash.observeAsState(listOf())
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Phone Books",
                        color = MaterialTheme.colors.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch { scaffoldState.drawerState.open() }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Drawer Button"
                        )
                    }
                }
            )
        },
        drawerContent = {
            AppDrawer(
                currentScreen = Screen.PhoneBooks,
                closeDrawerAction = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onCreateNewPhoneBookClick() },
                contentColor = MaterialTheme.colors.background,
                content = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Note Button"
                    )
                }
            )
        }
    ) {
        if (phoneBooks.isNotEmpty()) {
            PhoneBooksList(
                phoneBooks = phoneBooks.sortedBy { it.firstName },
                onPhoneBookCheckedChange = {
                    viewModel.onPhoneBookCheckedChange(it)
                },
                onPhoneBookClick = { viewModel.onPhoneBookClick(it) }
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun PhoneBooksList(
    phoneBooks: List<PhoneBookModel>,
    onPhoneBookCheckedChange: (PhoneBookModel) -> Unit,
    onPhoneBookClick: (PhoneBookModel) -> Unit
) {
    LazyColumn {
        items(count = phoneBooks.size) { noteIndex ->
            val phoneBook = phoneBooks[noteIndex]
            PhoneBook(
                phoneBook = phoneBook,
                onPhoneBookClick = onPhoneBookClick,
                onPhoneBookCheckedChange = onPhoneBookCheckedChange,
                isSelected = false
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun PhoneBooksListPreview() {
    PhoneBooksList(
        phoneBooks = listOf(
            PhoneBookModel(1, "Name 1", "Last name 1", "company 1"),
            PhoneBookModel(2, "Name 2", "Last name 2", "company 2"),
            PhoneBookModel(3, "Name 3", "Last name 3", "company 3")
        ),
        onPhoneBookCheckedChange = {},
        onPhoneBookClick = {}
    )
}