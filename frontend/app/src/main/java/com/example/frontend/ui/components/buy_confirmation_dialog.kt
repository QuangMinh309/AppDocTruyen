package com.example.frontend.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
                .border(0.5.dp, BurntCoral, shape = RoundedCornerShape(20.dp)),

            onDismissRequest = onDismiss,
           title = { Text(title, color = Color.White) },
            text = {
                Text(
                    text = text,//"Are you sure to buy this Story / Chapter with $price$ ?",
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
