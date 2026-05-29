package com.example.notepad.ui.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notepad.R

/**
 * Creates dropdown menu with action buttons for note card.
 *
 * @param state visibility state.
 * @param onDismissRequest dismiss request function.
 * @param onEdit edit note function.
 * @param onDelete delete note function.
 * @param onShare share note function.
 */
@Composable
private fun ActionsMenuList(
    state: Boolean,
    onDismissRequest: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit
) {
    DropdownMenu(
        expanded = state,
        onDismissRequest = { onDismissRequest() }
    ) {
        // edit note button
        DropdownMenuUiIconItem(
            onClick = { onEdit() },
            iconPainter = painterResource(R.drawable.outline_edit_24),
            contentDescription = null,
            text = "edit"
        )

        // delete note button
        DropdownMenuUiIconItem(
            onClick = { onDelete() },
            iconPainter = painterResource(R.drawable.baseline_delete_24),
            contentDescription = null,
            text = "delete"
        )

        HorizontalDivider() // divider

        // share note button
        DropdownMenuUiIconItem(
            onClick = { onShare() },
            iconPainter = painterResource(R.drawable.outline_share_24),
            contentDescription = null,
            text = "share"
        )
    }
}

/**
 * Creates card adapted to note entity data.
 *
 * @param onClick action, when clicked.
 * @param onEdit edit note function.
 * @param onDelete delete note function.
 * @param noteName note name.
 * @param noteDatetimeCreation note datetime creation.
 * @param noteLastEditDatetime note last edit datetime.
 */
@Composable
fun NoteUiCard(
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit,
    noteName: String,
    noteOrderNum: Int?,
    useBrightBg: Boolean,
    noteDatetimeCreation: String,
    noteLastEditDatetime: String?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor =
                if (useBrightBg) MaterialTheme.colorScheme.surfaceContainerHighest
                else MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = { onClick() })
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    Text(
                        text = noteName,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.basicMarquee(Int.MAX_VALUE)
                    )

                    Text(
                        text = noteDatetimeCreation,
                        fontWeight = FontWeight.Light,
                        fontSize = 10.sp,
                        modifier = Modifier.basicMarquee(Int.MAX_VALUE)
                    )

                    // note last edit datetime mark
                    noteLastEditDatetime?.let { dateTime ->
                        Spacer(modifier = Modifier.weight(1f))

                        Column {
                            Row {
                                Icon(
                                    painter = painterResource(R.drawable.outline_edit_24),
                                    contentDescription = null
                                )

                                Text(
                                    text = "last edit",
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 10.sp,
                                    modifier = Modifier
                                        .padding(start = 3.dp)
                                        .basicMarquee(Int.MAX_VALUE)
                                )
                            }

                            HorizontalDivider(
                                modifier = Modifier.width(130.dp),
                                color = if (isSystemInDarkTheme()) Color.White else Color.Black
                            )

                            Text(
                                text = dateTime,
                                fontWeight = FontWeight.Light,
                                fontSize = 10.sp,
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .basicMarquee(Int.MAX_VALUE)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // note order num
                    noteOrderNum?.let {
                        Text(
                            text = it.toString(),
                            fontWeight = FontWeight.Light,
                            fontSize = 10.sp
                        )
                    }
                }

                var dropdownMenuState by remember { mutableStateOf(false) }

                Box {
                    IconButton(onClick = { dropdownMenuState = true }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_more_vert_24),
                            contentDescription = null
                        )
                    }

                    // dropdown menu
                    ActionsMenuList(
                        state = dropdownMenuState,
                        onDismissRequest = { dropdownMenuState = false },
                        onEdit = {
                            dropdownMenuState = false // close dropdown menu
                            onEdit()
                        },
                        onDelete = {
                            dropdownMenuState = false // close dropdown menu
                            onDelete()
                        },
                        onShare = {
                            dropdownMenuState = false // close dropdown menu
                            onShare()
                        }
                    )
                }
            }
        }
    }
}