package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.markusw.chatgptapp.ui.theme.ChatGptAppTheme
import com.markusw.chatgptapp.ui.theme.DarkBlue
import com.markusw.chatgptapp.ui.theme.DarkBlue50

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onDone: () -> Unit,
    onGotToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss,
        content = {
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                color = DarkBlue
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PermissionHeader(
                        text = "Permission required",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Divider(modifier = Modifier.fillMaxWidth())
                    PermissionBody(
                        text = permissionTextProvider.getDescription(isPermanentlyDeclined),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Button(
                            onClick = { if (isPermanentlyDeclined) onGotToSettings() else onDone() },
                            shape = RoundedCornerShape(size = 4.dp),
                            content = {
                                Text(
                                    text = if (isPermanentlyDeclined) "Grant permission" else "OK",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkBlue50
                            )
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun PermissionHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.White
        )
    }
}

@Composable
fun PermissionBody(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Text(text = text, style = MaterialTheme.typography.bodyLarge, color = Color.White)
    }
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class RecordAudioPermissionProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) "It seems that you permanently declined record audio permission. \n" +
                "You can go to app settings to grant it."
        else "This app needs record audio permission to hear your voice."
    }

}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun PermissionDialogPreview() {
    ChatGptAppTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        PermissionDialog(
            permissionTextProvider = RecordAudioPermissionProvider(),
            isPermanentlyDeclined = false,
            onDismiss = {},
            onDone = {},
            onGotToSettings = {}
        )
    }
}
