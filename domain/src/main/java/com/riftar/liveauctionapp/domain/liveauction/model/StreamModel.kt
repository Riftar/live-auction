package com.riftar.liveauctionapp.domain.liveauction.model

data class StreamModel(
    val avatarUrl: String,
    val streamID: String,
    val userName: String,
    val viewCount: Int,
    val streamTitle: String,
    val streamDate: Long,
    val thumbnailUrl: String
)