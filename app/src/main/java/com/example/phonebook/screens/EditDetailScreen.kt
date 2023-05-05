package com.example.phonebook.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phonebook.routing.PhoneBooksRouter
import com.example.phonebook.routing.Screen
import com.example.phonebook.ui.components.ProfileColor
import com.example.phonebook.util.fromHex
import com.example.phonebook.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import com.example.phonebook.R.drawable
import com.example.phonebook.domain.model.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalMaterialApi
@Composable
fun EditDetailScreen(viewModel: MainViewModel) {
    val phoneBookEntry by viewModel.phoneBookEntry.observeAsState(PhoneBookModel())

    val profile: List<ProfileModel> by viewModel.profiles.observeAsState(listOf())

    val phoneLabel: List<PhoneLabelModel> by viewModel.phoneLabels.observeAsState(listOf())

    val emailLabel: List<EmailLabelModel> by viewModel.emailLabels.observeAsState(listOf())

    val bottomDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    val movePhoneBookToTrashDialogShownState = rememberSaveable { mutableStateOf(false) }

    BackHandler {
        if (bottomDrawerState.isOpen) {
            coroutineScope.launch { bottomDrawerState.close() }
        } else {
            PhoneBooksRouter.navigateTo(Screen.SeeDetail)
        }
    }

    Scaffold(
        topBar = {
            val isEditingMode: Boolean = phoneBookEntry.id != NEW_BOOK_ID
            EditDetailTopAppBar(
                isEditingMode = isEditingMode,
                onBackClick = { PhoneBooksRouter.navigateTo(Screen.SeeDetail) },
                onSaveNoteClick = { viewModel.savePhoneBook(phoneBookEntry) },
                onOpenColorPickerClick = {
                    coroutineScope.launch { bottomDrawerState.open() }
                },
                onDeleteNoteClick = {
                    movePhoneBookToTrashDialogShownState.value = true
                }
            )
        }
    ) {
        BottomDrawer(
            drawerState = bottomDrawerState,
            drawerContent = {
                ProfilePicker(
                    colors = profile,
                    onColorSelect = { color ->
                        viewModel.onPhoneBookEntryChange(phoneBookEntry.copy(profile = color))
                    }
                )
            }
        ) {
            EditPhoneBookDetail(
                phoneBook = phoneBookEntry,
                onPhoneBookChange = { updateNoteEntry ->
                    viewModel.onPhoneBookEntryChange(updateNoteEntry)
                },
                phoneLabels = phoneLabel,
                emailLabels = emailLabel,
            )
        }

        if (movePhoneBookToTrashDialogShownState.value) {
            AlertDialog(
                onDismissRequest = {
                    movePhoneBookToTrashDialogShownState.value = false
                },
                title = {
                    Text("Move note to the trash?")
                },
                text = {
                    Text(
                        "Are you sure you want to " +
                                "move this note to the trash?"
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
fun EditDetailTopAppBar(
    isEditingMode: Boolean,
    onBackClick: () -> Unit,
    onSaveNoteClick: () -> Unit,
    onOpenColorPickerClick: () -> Unit,
    onDeleteNoteClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Edit Detail",
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
            IconButton(onClick = onSaveNoteClick) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }

            IconButton(onClick = onOpenColorPickerClick) {
                Icon(
                    painter = painterResource(id = drawable.baseline_color_lens_24),
                    contentDescription = "Open Profile Color Picker Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }

            if (isEditingMode) {
                IconButton(onClick = onDeleteNoteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Note Button",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    )
}

@Composable
private fun EditPhoneBookDetail(
    phoneBook: PhoneBookModel,
    onPhoneBookChange: (PhoneBookModel) -> Unit,
    phoneLabels: List<PhoneLabelModel>,
    emailLabels: List<EmailLabelModel>,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PickedProfile(profile = phoneBook.profile)

        ContentTextField(
            label = "First Name",
            text = phoneBook.firstName,
            onTextChange = { newTitle ->
                onPhoneBookChange.invoke(phoneBook.copy(firstName = newTitle))
            }
        )

        ContentTextField(
            label = "Last Name",
            text = phoneBook.lastName,
            onTextChange = { newTitle ->
                onPhoneBookChange.invoke(phoneBook.copy(lastName = newTitle))
            }
        )

        ContentTextField(
            label = "Company",
            text = phoneBook.company,
            onTextChange = { newTitle ->
                onPhoneBookChange.invoke(phoneBook.copy(company = newTitle))
            }
        )

        ContentTextField(
            label = "Phone",
            text = phoneBook.phone,
            onTextChange = { newTitle ->
                onPhoneBookChange.invoke(phoneBook.copy(phone = newTitle))
            }
        )

        PickedPhoneLabel(phoneLabel = phoneBook.phoneLabel,
            label = { newLabel -> onPhoneBookChange.invoke(phoneBook.copy(phoneLabel = newLabel))},
            listLabel = phoneLabels)

        ContentTextField(
            label = "Email",
            text = phoneBook.email,
            onTextChange = { newTitle ->
                onPhoneBookChange.invoke(phoneBook.copy(email = newTitle))
            }
        )

        PickedEmailLabel( emailLabel = phoneBook.emailLabel,
            label = { newLabel -> onPhoneBookChange.invoke(phoneBook.copy(emailLabel = newLabel))},
            listLabel = emailLabels)
    }
}

@Composable
private fun ContentTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    onTextChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        )
    )
}

@Composable
private fun PickedProfile(profile: ProfileModel) {
    Row(
        Modifier
            .padding(8.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Picked profile color",
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        ProfileColor(
            color = Color.fromHex(profile.hex),
            size = 40.dp,
            border = 1.dp,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
private fun PickedPhoneLabel(phoneLabel: PhoneLabelModel,
                             label: (PhoneLabelModel) -> Unit,
                             listLabel: List<PhoneLabelModel>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedLabel by remember { mutableStateOf(label) }
    var labelName by remember { mutableStateOf(phoneLabel.name) }
    Row(
        Modifier
        .padding(8.dp)
        .padding(top = 16.dp))
    {
        Text("Selected Email Label: " + labelName,
        modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterVertically)
        )

        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false},
        ) {
            listLabel.forEach { item ->
                DropdownMenuItem(onClick = {
                    selectedLabel = label
                    label(item)
                    labelName = item.name
                }) {
                    Text(item.name)
                }
            }
        }
    }
}

@Composable
private fun PickedEmailLabel( emailLabel: EmailLabelModel,
    label: (EmailLabelModel) -> Unit , listLabel: List<EmailLabelModel>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedLabel by remember { mutableStateOf(label) }
    var labelName by remember { mutableStateOf(emailLabel.name) }
    Row(
        Modifier
            .padding(8.dp)
            .padding(top = 16.dp) )
    {
        Text("Selected Email Label: " + labelName,
                modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )

        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false},
        ) {
               listLabel.forEach { item ->
                   DropdownMenuItem(onClick = {
                       selectedLabel = label
                       label(item)
                       labelName = item.name
                   }) {
                       Text(item.name)
                   }
               }
        }

    }
}

@Composable
private fun ProfilePicker(
    colors: List<ProfileModel>,
    onColorSelect: (ProfileModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Profile color picker",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(colors.size) { itemIndex ->
                val color = colors[itemIndex]
                ProfileItem(
                    color = color,
                        onColorSelect = onColorSelect
                )
            }
        }
    }
}

@Composable
fun ProfileItem(
    color: ProfileModel,
    onColorSelect: (ProfileModel) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onColorSelect(color)
                }
            )
    ) {
        ProfileColor(
            modifier = Modifier.padding(10.dp),
            color = Color.fromHex(color.hex),
            size = 80.dp,
            border = 2.dp
        )
        Text(
            text = color.name,
            fontSize = 22.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Preview
@Composable
fun ProfileItemPreview() {
    ProfileItem(ProfileModel.DEFAULT) {}
}

@Preview
@Composable
fun ProfileColorPickerPreview() {
    ProfilePicker(
        colors = listOf(
            ProfileModel.DEFAULT,
            ProfileModel.DEFAULT,
            ProfileModel.DEFAULT
        )
    ) { }
}

@Preview
@Composable
fun PickedProfilePreview() {
    PickedProfile(ProfileModel.DEFAULT)
}