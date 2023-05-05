package com.example.phonebook.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phonebook.domain.model.NEW_BOOK_ID
import com.example.phonebook.domain.model.PhoneBookModel
import com.example.phonebook.domain.model.ProfileModel
import com.example.phonebook.routing.PhoneBooksRouter
import com.example.phonebook.routing.Screen
import com.example.phonebook.ui.components.ProfileColor
import com.example.phonebook.util.fromHex
import com.example.phonebook.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalMaterialApi
@Composable
fun SeeDetailScreen(viewModel: MainViewModel) {
    val phoneBookEntry by viewModel.phoneBookEntry.observeAsState(PhoneBookModel())

    val profile: List<ProfileModel> by viewModel.profiles.observeAsState(listOf())

    val bottomDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    val movePhoneBookToTrashDialogShownState = rememberSaveable { mutableStateOf(false) }

    BackHandler {
        if (bottomDrawerState.isOpen) {
            coroutineScope.launch { bottomDrawerState.close() }
        } else {
            PhoneBooksRouter.navigateTo(Screen.PhoneBooks)
        }
    }

    Scaffold(
        topBar = {
            val isEditingMode: Boolean = phoneBookEntry.id != NEW_BOOK_ID
            DetailTopAppBar(
                isEditingMode = isEditingMode,
                onBackClick = { PhoneBooksRouter.navigateTo(Screen.PhoneBooks) },
                onEditDetailClick = { PhoneBooksRouter.navigateTo(Screen.EditDetail) },
                onDeletePhoneBookClick = {
                    movePhoneBookToTrashDialogShownState.value = true
                }
            )
        }
    ) {

        PhoneBookDetail(
            phoneBook = phoneBookEntry,
        )


        if (movePhoneBookToTrashDialogShownState.value) {
            AlertDialog(
                onDismissRequest = {
                    movePhoneBookToTrashDialogShownState.value = false
                },
                title = {
                    Text("Move contact to the trash?")
                },
                text = {
                    Text(
                        "Are you sure you want to " +
                                "move this contact to the trash?"
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.movePhoneBookToTrash(phoneBookEntry)
                    }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        movePhoneBookToTrashDialogShownState.value = false
                    }) {
                        Text("Dismiss")
                    }
                }
            )
        }
    }
}

@Composable
fun DetailTopAppBar(
    isEditingMode: Boolean,
    onBackClick: () -> Unit,
    onEditDetailClick: () -> Unit,
    onDeletePhoneBookClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Detail",
                color = MaterialTheme.colors.onPrimary
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
        actions = {

            IconButton(onClick = onEditDetailClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Save contact Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }

            if (isEditingMode) {
                IconButton(onClick = onDeletePhoneBookClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete contact Button",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    )
}

@Composable
private fun PhoneBookDetail(
    phoneBook: PhoneBookModel,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(Modifier
            .padding(8.dp)
            .padding(top = 16.dp)
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileColor(color = Color.fromHex(phoneBook.profile.hex), size = 100.dp, border = 2.dp, modifier = Modifier.padding(4.dp))
            Text(text = "First Name: " + phoneBook.firstName,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            Text(text = "Last Name: " + phoneBook.lastName,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            Text(text = "Company: " + phoneBook.company,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            Text(text = "Phone (" + phoneBook.phoneLabel.name + "): " + phoneBook.phone,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            Text(text = "Email (" + phoneBook.emailLabel.name + "): " + phoneBook.email,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

