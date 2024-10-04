package com.riftar.liveauctionapp.liveauction

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.riftar.liveauctionapp.domain.liveauction.model.AuctionItem
import com.riftar.liveauctionapp.domain.liveauction.model.BidDetail
import com.riftar.liveauctionapp.domain.liveauction.model.LiveComment
import com.riftar.liveauctionapp.liveauction.ui.theme.LiveAuctionAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class LiveAuctionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveAuctionAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

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
                    .size(16.dp), tint = Color.DarkGray
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stream.viewCount.toString(), style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.DarkGray,
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
                BottomSheet(bidHistory, it, timeRemaining,
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

@Composable
fun StarterText(modifier: Modifier, startingCountDownTimer: Int) {
    if (startingCountDownTimer > 0) {
        Column(
            modifier = modifier
                .background(
                    color = Color.DarkGray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Starting In",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = startingCountDownTimer.toString(),
                style = MaterialTheme.typography.headlineLarge.copy(color = Color.White)
            )
        }
    }
}

@Composable
fun CommentSection(
    modifier: Modifier,
    listComment: List<LiveComment>,
    onSendComment: (String) -> Unit
) {
    var comment by rememberSaveable { mutableStateOf("") }

    Column(modifier = modifier) {
        Box(modifier = Modifier.height(300.dp)) {
            LazyColumn(
                modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
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
                        onSendComment(comment)
                        comment = ""
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Send, "", tint = Color.White)
                    }
                },
                singleLine = true
            )
            IconButton(modifier = Modifier.weight(0.2f), onClick = { /*TODO*/ }) {
                BadgedBox(badge = { Badge { Text("1") } }) {
                    Icon(Icons.Outlined.Storefront, "icon store", tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    bidHistory: List<BidDetail>,
    item: AuctionItem,
    timeRemaining: Int,
    onDismiss: () -> Unit,
    onTimeUp: () -> Unit,
    onBidPlaced: (Int) -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
            ItemDetail(bidHistory, item, timeRemaining,
                onTimeUp = {
                    onTimeUp.invoke()
                },
                onBidPlaced = { bidAmount ->
                    onBidPlaced.invoke(bidAmount)
                })
        }
    }
}

@Composable
fun ItemDetail(
    bidHistory: List<BidDetail>,
    item: AuctionItem,
    timeRemaining: Int,
    onTimeUp: () -> Unit,
    onBidPlaced: (Int) -> Unit
) {
    var timeLeft by rememberSaveable { mutableStateOf(timeRemaining) }
    val chatListState = rememberLazyListState()

    LaunchedEffect(bidHistory) {
        chatListState.animateScrollToItem(chatListState.layoutInfo.totalItemsCount)
    }
    LaunchedEffect(item) {
        timeLeft = timeRemaining
    }
    LaunchedEffect(key1 = timeLeft) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
        if (timeLeft == 0) {
            onTimeUp.invoke()
        }
    }

    Row(
        modifier = Modifier.padding(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = "avatar",
            modifier = Modifier
                .size(84.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyColumn(modifier = Modifier.weight(1f), state = chatListState) {
            items(bidHistory.size) {
                ItemBidding(bid = bidHistory.get(index = it))
            }
        }
        Card(
            border = BorderStroke(1.dp, Color.LightGray),
            colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Highest Bid",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "RM${item.currentPrice}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Card(
            border = BorderStroke(1.dp, Color.LightGray),
            colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ending in",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = timeLeft.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
    Button(
        onClick = {
            onBidPlaced.invoke(item.currentPrice + 5)
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color("#C2FF61".toColorInt()))
    ) {
        Text(
            text = "Bid RM${item.currentPrice + 5}",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun ItemBidding(bid: BidDetail) {
    Row(
        modifier = Modifier.padding(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = bid.avatar,
            contentDescription = "avatar",
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${bid.userName} is bidding RM${bid.amount.toInt()}",
            style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
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

@Composable
fun EndAuctionDialog(shouldShowDialog: MutableState<Boolean>, onDismiss: () -> Unit) {
    if (shouldShowDialog.value) {
        AlertDialog(
            onDismissRequest = {
                onDismiss.invoke()
            },

            title = { Text(text = "Times Up!") },
            text = { Text(text = "The item has been sold!") },
            confirmButton = {
                Button(
                    onClick = {
                        onDismiss.invoke()
                    }
                ) {
                    Text(
                        text = "Next Item",
                        color = Color.White
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LiveAuctionAppTheme {
        HomeScreen()
    }
}