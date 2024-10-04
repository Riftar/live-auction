package com.riftar.liveauctionapp.data.liveauction.response

import com.riftar.liveauctionapp.domain.liveauction.model.AuctionItem

data class AuctionItemResponse(
    val id: String?,
    val name: String?,
    val description: String?,
    val imageUrl: String?,
    val basePrice: Int?,
    var currentPrice: Int?,
    var currentBidder: String? = "",
    var timeRemaining: Int? = 10
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        0,
        0,
        "",
        10
    )

    companion object {
        fun AuctionItemResponse?.toDomainModel() = AuctionItem(
            id = this?.id.orEmpty(),
            name = this?.name.orEmpty(),
            description = this?.description.orEmpty(),
            imageUrl = this?.imageUrl.orEmpty(),
            basePrice = this?.basePrice ?: 0,
            currentPrice = this?.currentPrice ?: 0,
            currentBidder = this?.currentBidder.orEmpty(),
            timeRemaining = this?.timeRemaining ?: 10
        )
    }
}