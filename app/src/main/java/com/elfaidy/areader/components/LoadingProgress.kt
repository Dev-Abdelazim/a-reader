package com.elfaidy.areader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadingProgress(
    text: String
) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,

    ) {
        CircularProgressIndicator(
            modifier = Modifier.padding(
                top = 16.dp,
                start = 16.dp,
                bottom = 16.dp,
            )
        )

        Text(
            text = text,
            modifier = Modifier.padding(start = 14.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }

}