package com.lucreziacarena.mycoachassistant.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.lucreziacarena.mycoachassistant.R

@Composable
fun errorDialog(
    showErrorDialog: MutableState<Boolean>,
    errorMessage: MutableState<String>
) {
    AlertDialog(
        onDismissRequest = {
            showErrorDialog.value = false
        },
        title = {
            Text(text = stringResource(R.string.warning))
        },
        text = {
            Text(errorMessage.value)
        },
        confirmButton = {
            showErrorDialog.value = false
        }
    )
}
