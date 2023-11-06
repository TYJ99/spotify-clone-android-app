package com.tyj.spotifycloneandroidapp.presentation.screens.home.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.tyj.spotifycloneandroidapp.common.TopBarBackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopBar(
    toggleState: Boolean,
    onToggle: (Boolean) -> Unit,
    onBackPressed: () -> Unit,
) {
    // state of the menu
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(text = "Spotify Clone")
        },
        navigationIcon = {
            TopBarBackButton(onBackPressed)
        },

        actions = {

            // 3 vertical dots icon
            IconButton(
                onClick = {
                    expanded = true
                },
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Open Options",
                )
            }

            // drop down menu
            DropdownMenu(
                modifier = Modifier
                    .width(250.dp)
                    .padding(4.dp),
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
                // adjust the position
                //offset = DpOffset(x = (-102).dp, y = (-64).dp),
                offset = DpOffset(x = (0).dp, y = (0).dp),
                properties = PopupProperties()
            ) {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                    },
                    enabled = false,
                    leadingIcon = {
                        Icon(
                          imageVector = Icons.Default.SmartDisplay,
                          contentDescription = "leadingIcon",
                          modifier = Modifier.size(30.dp)
                        )
                    },
                    trailingIcon = {
                        IconToggleButton(
                            checked = toggleState,
                            onCheckedChange = { isChecked ->
                                onToggle(isChecked)
                            },
                            enabled = true,
                            interactionSource = MutableInteractionSource(),
                            content = {
                                if(toggleState) {
                                    Icon(
                                        imageVector = Icons.Default.ToggleOn,
                                        contentDescription = "toggle on",
                                        modifier = Modifier.size(56.dp),
                                    )
                                }else {
                                    Icon(
                                        imageVector = Icons.Default.ToggleOff,
                                        contentDescription = "toggle off",
                                        modifier = Modifier.size(56.dp),
                                    )
                                }

                            }

                        )
                    },
                    text = {
                        Text(
                            text = "Traditional Player",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                        )
                    }
                )
            }
        }
    )
}

@Preview
@Composable
fun SongScreenTopBarPrev() {
    HomeScreenTopBar(
        toggleState = false,
        onToggle = { _ -> Unit },
        onBackPressed = {}
    )
}
