package com.riftar.liveauctionapp.liveauction

import androidx.lifecycle.ViewModel

class LiveAuctionViewModel : ViewModel() {

}


data class StreamModel(
    val avatarUrl: String,
    val streamID: String,
    val userName: String,
    val viewCount: Int,
    val streamTitle: String,
    val streamDate: Long,
    val thumbnailUrl: String
)

data class Comment(
    val id: Int,
    val author: String,
    val comment: String,
    val avatar: Int,
    val date: Long
)

data class ItemDetail(
    val id: Int,
    val name: String,
    val description: String,
    val photo: Int
)

val camera = ItemDetail(
    id = 7413,
    name = "Sony A7x II",
    description = "Capture stunning photos and videos with our compact and versatile mirrorless camera. Enjoy interchangeable lenses and advanced features for professional-grade results.",
    photo = R.drawable.ic_camera
)

val chair = ItemDetail(
    id = 4335,
    name = "Stramm Ergonomic Chair",
    description = "Upgrade your workspace with our ergonomic chair. Designed for comfort and support, this chair features adjustable settings and breathable fabric.",
    photo = R.drawable.ic_chair
)

val mouse = ItemDetail(
    id = 4335,
    name = "Rex Gaming Mouse",
    description = "Experience precise control and comfort with our high-performance mouse. Customizable settings and ergonomic design make it ideal for work and play.",
    photo = R.drawable.ic_mouse
)

val listItem = listOf(
    camera,
    chair,
    mouse
)

data class BidDetail(
    val id: Int,
    val avatar: Int,
    val userName: String,
    val bidAmount: Int,
    val date: Long
)