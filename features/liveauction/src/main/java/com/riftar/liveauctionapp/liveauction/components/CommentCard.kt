package com.riftar.liveauctionapp.liveauction.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.riftar.liveauctionapp.domain.liveauction.model.LiveComment

@Composable
fun CommentCard(comment: LiveComment) {
    Row(
        modifier = Modifier.padding(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = comment.avatar,
            contentDescription = "avatar",
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = comment.author,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment.comment,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
            )
        }
    }
}