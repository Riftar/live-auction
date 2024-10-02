package com.riftar.liveauctionapp.domain.liveauction.model
data class AuctionItem(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val basePrice: Int,
    var currentPrice: Int,
    var currentBidder: String = "",
    var timeRemaining: Int = 10
) {
    constructor(): this(
        "",
        "",
        "",
        "",
        0,
        0,
        "",
        10
    )
}