package com.elfaidy.areader.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun RoundedButton(
    label: String,
    onPress: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxSize()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        Button(
            onClick = onPress,
            modifier = Modifier.clip(
                RoundedCornerShape(
                    bottomEnd = 28.dp,
                    topStart = 28.dp,
                )
            ).padding(0.dp),
            shape = RectangleShape
        ) {

            Text(
                text = label
            )
        }
    }
}