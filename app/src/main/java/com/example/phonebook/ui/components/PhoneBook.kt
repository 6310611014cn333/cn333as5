package com.example.phonebook.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.phonebook.domain.model.PhoneBookModel
import com.example.phonebook.util.fromHex

@ExperimentalMaterialApi
@Composable
fun PhoneBook(
    modifier: Modifier = Modifier,
    phoneBook: PhoneBookModel,
    onPhoneBookClick: (PhoneBookModel) -> Unit = {},
    onPhoneBookCheckedChange: (PhoneBookModel) -> Unit = {},
    isSelected: Boolean
) {
    val background = if (isSelected)
        Color.LightGray
    else
        MaterialTheme.colors.surface

    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        backgroundColor = background
    ) {
        ListItem(
            text = { Text(text = phoneBook.firstName + " " + phoneBook.lastName, maxLines = 1) },
            secondaryText = {
                Text(text = phoneBook.company, maxLines = 1)
            },
            icon = {
                ProfileColor(
                    color = Color.fromHex(phoneBook.profile.hex),
                    size = 40.dp,
                    border = 1.dp
                )
            },
            modifier = Modifier.clickable {
                onPhoneBookClick.invoke(phoneBook)
            }
        )
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun PhoneBookPreview() {
    PhoneBook(phoneBook = PhoneBookModel(1, "Chawanrat", "Sabenjapakin",
        "Thammsat", "0612656694"), isSelected = true)
}