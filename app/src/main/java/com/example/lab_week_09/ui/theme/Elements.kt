package com.example.lab_week_09.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//UI Element for displaying a title
@Composable
fun OnBackgroundTitleText(text: String) {
    TitleText(text = text, color =
        MaterialTheme.colorScheme.onBackground)
}
//Here, we use the titleLarge style from the typography
@Composable
fun TitleText(text: String, color: Color) {
    Text(text = text, style =
        MaterialTheme.typography.titleLarge, color = color)
}

//UI Element for displaying an item list
@Composable
fun OnBackgroundItemText(text: String) {
    ItemText(text = text, color =
        MaterialTheme.colorScheme.onBackground)
}
//Here, we use the bodySmall style from the typography
@Composable
fun ItemText(text: String, color: Color) {
    Text(text = text, style =
        MaterialTheme.typography.bodySmall, color = color)
}
//UI Element for displaying a button
@Composable
fun PrimaryTextButton(text: String, onClick: () -> Unit, enabled: Boolean = true) { // <-- ADDED enabled parameter
    TextButton(text = text,
        textColor = Color.White,
        onClick = onClick,
        enabled = enabled // Pass the new enabled state
    )
}
//Here, we use the labelMedium style from the typography
@Composable
fun TextButton(text: String, textColor: Color, onClick: () -> Unit, enabled: Boolean = true) { // <-- ADDED enabled parameter
    Button(
        onClick = onClick,
        modifier = Modifier.padding(8.dp),
        enabled = enabled, // <-- Used the enabled state here
        colors = ButtonDefaults
            .buttonColors(
                containerColor = Color.DarkGray,
                contentColor = textColor,
                // Provide a custom disabled color for better visual feedback
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White.copy(alpha = 0.5f)
            )
    ) {
        Text(text = text, style =
            MaterialTheme.typography.labelMedium)
    }
}