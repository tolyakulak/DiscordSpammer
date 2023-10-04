package com.github.tolyakulak.discordspammer.feature_login.presentation.exist_login.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.tolyakulak.discordspammer.feature_login.domain.model.Token

/**
 * Item for [Token] in lazy column of login screen
 * @param token [Token] to display in this item
 * @param onClick block of code to execute when user clicks this item
 * @param onDelete delete button action. must delete this [token]
 */
@Composable
fun LoginLazyItem(
    token: Token,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Row {
        Button(onClick = onClick) {
            Text(
                text = token.username ?: token.token
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                Icons.Default.Delete,
                contentDescription = null
            )
        }
    }
    Spacer(Modifier.height(8.dp))
}