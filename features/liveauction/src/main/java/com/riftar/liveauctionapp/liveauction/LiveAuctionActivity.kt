package com.riftar.liveauctionapp.liveauction

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import coil.compose.AsyncImage
import com.riftar.liveauctionapp.liveauction.ui.theme.LiveAuctionAppTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
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
//    val viewModel: LiveAuctionViewModel = viewModel()
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
        }/450/800"
    )
    val constraint = itemStreamConstraints()
    ConstraintLayout(constraint) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .layoutId("image"),
            contentScale = ContentScale.Crop,
            model = stream.thumbnailUrl,
            contentDescription = "image thumbnail"
        )
        Row(
            modifier = Modifier
                .padding(8.dp)
                .layoutId("viewCount")
                .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
                .padding(2.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.Visibility, contentDescription = "eye icon", modifier = Modifier
                    .width(16.dp)
                    .height(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stream.viewCount.toString(), style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
        Row(
            modifier = Modifier
                .padding(8.dp)
                .layoutId("userInfo")
                .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
                .padding(4.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop,
                model = stream.avatarUrl,
                contentDescription = "avatar"
            )
            Column {
                Text(
                    text = stream.userName,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .layoutId("streamer")
                        .padding(horizontal = 8.dp)
                )
                Text(
                    text = stream.streamTitle,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .layoutId("title")
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}

private fun itemStreamConstraints() = ConstraintSet {
    val viewCount = createRefFor("viewCount")
    val userInfo = createRefFor("userInfo")

    constrain(viewCount) {
        top.linkTo(parent.top)
        end.linkTo(parent.end)
    }
    constrain(userInfo) {
        top.linkTo(parent.top)
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