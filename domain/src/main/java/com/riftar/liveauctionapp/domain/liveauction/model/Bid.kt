package com.riftar.liveauctionapp.domain.liveauction.model

data class Bid(
    val userName: String,
    val amount: Double,
    val timestamp: Long = System.currentTimeMillis()
)