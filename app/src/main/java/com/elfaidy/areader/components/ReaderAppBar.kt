@file:OptIn(ExperimentalMaterial3Api::class)

package com.elfaidy.areader.components

import androidx.annotation.DrawableRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.elfaidy.areader.R

@Composable
fun ReaderAppBar(
    title: String,
    @DrawableRes navIcon: Int,
    actionIcon: ImageVector? = null,
    onNavIconClick: () -> Unit,
    onActionIconClick: () -> Unit = {},
) {

    TopAppBar(
        title = {
            Text(text = title)
        },
        actions = {
            if (actionIcon != null){
                IconButton(
                    onClick = onActionIconClick,
                ) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = actionIcon.name
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onNavIconClick
            ) {
                Icon(
                    painter = painterResource(id = navIcon),
                    contentDescription = stringResource(id = R.string.app_name)
                )
            }
        }

    )

}