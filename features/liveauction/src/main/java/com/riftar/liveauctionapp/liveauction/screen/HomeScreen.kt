package com.riftar.liveauctionapp.liveauction.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.riftar.liveauctionapp.liveauction.LiveAuctionViewModel
import com.riftar.liveauctionapp.liveauction.components.AuctionItemBottomSheet
import com.riftar.liveauctionapp.liveauction.components.CommentSection
import com.riftar.liveauctionapp.liveauction.components.EndAuctionDialog
import com.riftar.liveauctionapp.liveauction.components.StarterText
import kotlinx.coroutines.delay


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val viewModel: LiveAuctionViewModel = hiltViewModel()
    val currentItem by viewModel.auctionItem.collectAsState()
    val timeRemaining by viewModel.timeRemaining.collectAsState()
    val stream by viewModel.stream.collectAsState()
    val listComment by viewModel.listComment.collectAsState()
    val bidHistory by viewModel.bidHistory.collectAsState()
    var showItemAuctionSheet by rememberSaveable { mutableStateOf(false) }
    val showEndAuctionDialog = rememberSaveable { mutableStateOf(false) }
    val deviceManufacturer = android.os.Build.MANUFACTURER
    var startingCountDownTimer by rememberSaveable { mutableIntStateOf(10) }
    val constraint = itemStreamConstraints()

    LaunchedEffect(Unit) {
        viewModel.getAuctionItem()
        viewModel.getTimeRemaining()
        viewModel.getListComment()
    }
    LaunchedEffect(key1 = startingCountDownTimer) {
        while (startingCountDownTimer > 0) {
            delay(1000L)
            startingCountDownTimer--
        }
        if (startingCountDownTimer == 0) {
            showItemAuctionSheet = true
        }
    }

    ConstraintLayout(constraint, modifier = modifier) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .layoutId("image")
                .background(color = Color.DarkGray),
            contentScale = ContentScale.Crop,
            model = stream.thumbnailUrl,
            contentDescription = "image thumbnail"
        )
        Row(
            modifier = Modifier
                .padding(16.dp)
                .layoutId("viewCount")
                .background(color = Color.LightGray, shape = RoundedCornerShape(16.dp))
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.Visibility, contentDescription = "eye icon", modifier = Modifier
                    .size(16.dp), tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stream.viewCount.toString(), style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .background(color = Color.Red, shape = RoundedCornerShape(16.dp))
                    .padding(
                        horizontal = 2.dp
                    )
            ) {
                Text(
                    text = "Live",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(8.dp)
                .layoutId("userInfo")
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color = Color.White),
                contentScale = ContentScale.Crop,
                model = stream.avatarUrl,
                contentDescription = "avatar",
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = stream.userName,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White),
                )
                Text(
                    text = stream.streamTitle,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                )
            }
        }

        StarterText(modifier = Modifier.layoutId("starterText"), startingCountDownTimer)

        CommentSection(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .layoutId("commentSection"),
            listComment,
            onSendComment = { comment ->
                viewModel.sendComment(deviceManufacturer, comment)
            }
        )

        if (showItemAuctionSheet) {
            currentItem?.let {
                viewModel.getBidHistory(it.id)
                AuctionItemBottomSheet(bidHistory, it, timeRemaining,
                    onTimeUp = {
                        showEndAuctionDialog.value = true
                    },
                    onBidPlaced = {
                        currentItem?.let { item ->
                            viewModel.placeBid(deviceManufacturer, item)
                        }
                    },
                    onDismiss = {
                        showItemAuctionSheet = false
                    })
            }
        }

        EndAuctionDialog(showEndAuctionDialog,
            onDismiss = {
                showEndAuctionDialog.value = false
                viewModel.goToNextItem()
                showItemAuctionSheet = false
                startingCountDownTimer = 5
            }
        )
    }
}


private fun itemStreamConstraints() = ConstraintSet {
    val viewCount = createRefFor("viewCount")
    val userInfo = createRefFor("userInfo")
    val commentSection = createRefFor("commentSection")
    val starterText = createRefFor("starterText")

    constrain(viewCount) {
        top.linkTo(parent.top)
        end.linkTo(parent.end)
    }
    constrain(userInfo) {
        top.linkTo(parent.top)
        start.linkTo(parent.start)
    }
    constrain(commentSection) {
        bottom.linkTo(parent.bottom)
        start.linkTo(parent.start)
    }
    constrain(starterText) {
        top.linkTo(userInfo.bottom)
        bottom.linkTo(commentSection.top)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
    }
}
