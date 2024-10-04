package com.riftar.liveauctionapp.liveauction.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.riftar.liveauctionapp.domain.liveauction.model.LiveComment

@Composable
fun CommentSection(
    modifier: Modifier,
    listComment: List<LiveComment>,
    onSendComment: (String) -> Unit
) {
    var comment by rememberSaveable { mutableStateOf("") }
    val chatListState = rememberLazyListState()

    LaunchedEffect(listComment) {
        chatListState.animateScrollToItem(chatListState.layoutInfo.totalItemsCount)
    }
    Column(modifier = modifier) {
        Box(modifier = Modifier.height(300.dp)) {
            LazyColumn(
                modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                state = chatListState
            ) {
                items(listComment.size) {
                    CommentCard(comment = listComment.get(index = it))
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .onKeyEvent { event ->
                        if (event.key == Key.Enter && event.type == KeyEventType.KeyUp) {
                            if (comment.isNotBlank()) {
                                onSendComment(comment)
                                comment = ""
                            }
                            true
                        } else {
                            false
                        }

                    }, value = comment, onValueChange = { comment = it },

                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (comment.isNotBlank()) {
                            onSendComment(comment)
                            comment = ""
                        }
                    }
                ),
                label = {},
                placeholder = {
                    Text(
                        text = "Say something",
                        style = TextStyle(color = Color.White)
                    )
                },
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        if (comment.isNotBlank()) {
                            onSendComment(comment)
                            comment = ""
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Send, "", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                singleLine = true
            )
            IconButton(modifier = Modifier.weight(0.2f), onClick = { /*TODO*/ }) {
                BadgedBox(badge = { Badge { Text("1") } }) {
                    Icon(Icons.Outlined.Storefront, "icon store", tint = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}
