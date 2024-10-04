package com.riftar.liveauctionapp.liveauction.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState


@Composable
fun EndAuctionDialog(shouldShowDialog: MutableState<Boolean>, onDismiss: () -> Unit) {
    if (shouldShowDialog.value) {
        AlertDialog(
            onDismissRequest = {
                onDismiss.invoke()
            },

            title = { Text(text = "Times Up!") },
            text = { Text(text = "The item has been sold!") },
            confirmButton = {
                Button(
                    onClick = {
                        onDismiss.invoke()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Next Item",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        )
    }
}