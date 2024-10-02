package com.riftar.liveauctionapp.domain.liveauction.model

data class Bid(
    val userId: String,
    val amount: Double,
    val timestamp: Long = System.currentTimeMillis()
)