package com.example.frontend.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
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
fun BuyConfirmationDialog(
    showDialog: Boolean,
    price: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Buy Story / Chapter", color = Color.White) },
            text = {
                Text(
                    "Are you sure to buy this Story / Chapter with $price$ ?",
                    color = Color.White
                )
            },
            confirmButton = {
                Row {
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(containerColor = Green),
                        modifier = Modifier.weight(0.5f)
                    ) {
                        Text("Yes")
                    }
                    Spacer(modifier = Modifier.width(43.dp))
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = BurntCoral),
                        modifier = Modifier.weight(0.5f)
                    ) {
                        Text("No")
                    }
                }
            },
            containerColor = Color.Black
        )
    }
}
