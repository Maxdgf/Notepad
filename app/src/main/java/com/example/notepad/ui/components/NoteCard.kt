package com.example.notepad.ui.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notepad.R

/**
 * Creates card adapted to note entity data.
 * @param onClick on click function.
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
    noteDatetimeCreation: String,
    noteLastEditDatetime: String? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable(
                onClick = { onClick() }
            )
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
            }

            Column {
                IconButton(onClick = { onEdit() }) {
                    Icon(
                        painter = painterResource(R.drawable.outline_edit_24),
                        contentDescription = null
                    )
                }

                IconButton(onClick = { onDelete() }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_delete_24),
                        contentDescription = null
                    )
                }

                IconButton(onClick = { onShare() }) {
                    Icon(
                        painter = painterResource(R.drawable.outline_share_24),
                        contentDescription = null
                    )
                }
            }
        }
    }
}