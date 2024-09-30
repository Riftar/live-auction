package com.riftar.liveauctionapp.liveauction

import androidx.lifecycle.ViewModel

class LiveAuctionViewModel: ViewModel() {

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