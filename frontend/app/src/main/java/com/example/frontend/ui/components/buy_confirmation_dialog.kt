package com.example.frontend.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.Green

@Composable
fun ConfirmationDialog(
    showDialog: Boolean,
    title:String,
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, BurntCoral, shape = RoundedCornerShape(25.dp)),

            onDismissRequest = onDismiss,
           title = { Text(title, color = Color.White) },
            text = {
                Text(
                    text = text,
                    color = Color.White
                )
            },
            confirmButton = {
                Row {
                    Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = BurntCoral),
                    modifier = Modifier.weight(0.5f)
                    ) {
                        Text("No",color = Color.Black)
                    }

                    Spacer(modifier = Modifier.width(43.dp))
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(containerColor = Green),
                        modifier = Modifier.weight(0.5f)
                    ) {
                        Text("Yes",color = Color.Black)
                    }

                }
            },
            containerColor = Color.Black
        )
    }
}

@Composable
fun InputDialog(
    showDialog: Boolean,
    title: String,
    initialReadListName: String = "", // Giá trị khởi tạo cho tên
    initialDescription: String = "", // Giá trị khởi tạo cho mô tả
    onConfirm: (readListName: String, description: String) -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        val readListName = remember { mutableStateOf(initialReadListName) }
        val description = remember { mutableStateOf(initialDescription) }

        AlertDialog(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, BurntCoral, shape = RoundedCornerShape(25.dp)),
            onDismissRequest = onDismiss,
            title = { Text(title, color = Color.White) },
            text = {
                Column(
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = readListName.value,
                        onValueChange = { readListName.value = it },
                        label = { Text("Read List Name", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Black,
                            unfocusedContainerColor = Color.Black,
                            focusedIndicatorColor = BurntCoral,
                            unfocusedIndicatorColor = BurntCoral,
                            cursorColor = BurntCoral
                        ),
                        textStyle = TextStyle(color = Color.White),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    TextField(
                        value = description.value,
                        onValueChange = { description.value = it },
                        label = { Text("Description", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Black,
                            unfocusedContainerColor = Color.Black,
                            focusedIndicatorColor = BurntCoral,
                            unfocusedIndicatorColor = BurntCoral,
                            cursorColor = BurntCoral
                        ),
                        textStyle = TextStyle(color = Color.White),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = BurntCoral),
                        modifier = Modifier.weight(0.5f)
                    ) {
                        Text("Cancel", color = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { onConfirm(readListName.value, description.value) },
                        colors = ButtonDefaults.buttonColors(containerColor = Green),
                        modifier = Modifier.weight(0.5f)
                    ) {
                        Text("Confirm", color = Color.Black)
                    }
                }
            },
            containerColor = Color.Black
        )
    }
}