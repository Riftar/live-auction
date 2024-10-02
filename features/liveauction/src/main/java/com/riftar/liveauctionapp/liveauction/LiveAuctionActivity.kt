package com.riftar.liveauctionapp.liveauction

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.riftar.liveauctionapp.liveauction.ui.theme.LiveAuctionAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random
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
    var showSheet by rememberSaveable { mutableStateOf(false) }
    val stream = StreamModel(
        avatarUrl = "https://picsum.photos/id/${
            Random.nextInt(1, 100)
        }/200/200",
        streamID = "stream19",
        userName = "NaturePhotographer",
        viewCount = 34567,
        streamTitle = "Nature Photography Tips",
        streamDate = 1667776000000, // April 2022
        thumbnailUrl = "https://picsum.photos/id/${
            Random.nextInt(1, 100)
        }/1080/1920/?blur=1"
    )
    val constraint = itemStreamConstraints()
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
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
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
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
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
                    text = currentItem.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                )
            }
        }
        CommentSection(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .layoutId("commentSection")
        )

        if (showSheet) {
            BottomSheet {
                showSheet = false
            }
        }

        // TODO DELETE
        Button(
            onClick = { showSheet = true }, modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
                .layoutId("buttonShow")
        ) {
            Text("Show Sheet")
        }
    }
}

@Composable
fun CommentSection(modifier: Modifier) {
    var comment by rememberSaveable { mutableStateOf("") }
    val listComments = listOf(
        Comment(
            id = 1,
            author = "Sheryl",
            comment = "The base ScrollScope where the scroll session was created.",
            avatar = R.drawable.ic_avatar_1,
            date = 3021
        ),
        Comment(
            id = 2,
            author = "Adele",
            comment = "logic describing fling behavior.",
            avatar = R.drawable.ic_avatar_3,
            date = 3021
        ),
        Comment(
            id = 3,
            author = "Robert",
            comment = "An implementation of LazyLayoutScrollScope that works with LazyRow and LazyColumn.",
            avatar = R.drawable.ic_avatar_2,
            date = 3021
        ),
        Comment(
            id = 4,
            author = "Adele",
            comment = "the vertical alignment applied to the items",
            avatar = R.drawable.ic_avatar_3,
            date = 3021
        ),
        Comment(
            id = 5,
            author = "Sheryl",
            comment = "the state object to be used to control or observe the list's state",
            avatar = R.drawable.ic_avatar_1,
            date = 3021
        ),
        Comment(
            id = 6,
            author = "Robert",
            comment = "a factory of the content types for the item. The item compositions of the same type could be reused more efficiently. Note that null is a valid type and items of such type will be considered compatible.",
            avatar = R.drawable.ic_avatar_2,
            date = 3021
        ),
    )
    val doubleList = mutableListOf<Comment>().apply {
        repeat(10) {
            addAll(listComments)
        }
    }
    Column(modifier = modifier) {
        Box(modifier = Modifier.height(300.dp)) {
            LazyColumn() {
                items(doubleList.size) {
                    CommentCard(comment = doubleList.get(index = it))
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(), value = comment, onValueChange = { comment = it },
            label = {},
            placeholder = { Text(text = "Say something", style = TextStyle(color = Color.White)) },
            shape = RoundedCornerShape(16.dp),
            trailingIcon = {
                Icon(Icons.AutoMirrored.Filled.Send, "", tint = Color.White)
            },
            singleLine = true
        )
    }
}

@Composable
fun CommentCard(comment: Comment) {
    Row(
        modifier = Modifier.padding(all = 8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(comment.avatar),
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
fun BottomSheet(onDismiss: () -> Unit) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        BidSection()
    }
}

@Composable
fun BidSection() {
    Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
        val item = listItem.random()
        ItemDetail(item)
    }
}

@Composable
fun ItemDetail(item: ItemDetail) {
    Row(
        modifier = Modifier.padding(all = 8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(item.photo),
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
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        val listBid = listOf(
            BidDetail(
                id = 1,
                avatar = R.drawable.ic_avatar_1,
                userName = "Adele",
                bidAmount = 25,
                date = 3021
            ),
            BidDetail(
                id = 2,
                avatar = R.drawable.ic_avatar_3,
                userName = "Raymond",
                bidAmount = 35,
                date = 3021
            ),
            BidDetail(
                id = 1,
                avatar = R.drawable.ic_avatar_2,
                userName = "Michael",
                bidAmount = 45,
                date = 3021
            )
        )
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(listBid.size) {
                ItemBidding(bid = listBid.get(index = it))
            }
        }
        Card(
            border = BorderStroke(1.dp, Color.LightGray),
            colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Highest Bid",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "RM 25",
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
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ending in",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "00:10",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color("#C2FF61".toColorInt()))
    ) {
        Text(
            text = "Bid",
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
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(bid.avatar),
            contentDescription = "avatar",
            modifier = Modifier
                .size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${bid.userName} is bidding RM ${bid.bidAmount}",
            style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
        )
    }
}

private fun itemStreamConstraints() = ConstraintSet {
    val viewCount = createRefFor("viewCount")
    val userInfo = createRefFor("userInfo")
    val commentSection = createRefFor("commentSection")
    //TODO DELETE
    val buttonShow = createRefFor("buttonShow")

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
    constrain(buttonShow) {
        bottom.linkTo(parent.bottom)
        start.linkTo(parent.start)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LiveAuctionAppTheme {
        HomeScreen()
    }
}