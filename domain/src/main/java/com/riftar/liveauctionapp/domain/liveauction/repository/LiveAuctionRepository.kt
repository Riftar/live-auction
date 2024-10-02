package com.riftar.liveauctionapp.domain.liveauction.repository

import com.riftar.liveauctionapp.domain.liveauction.model.AuctionItem
import com.riftar.liveauctionapp.domain.liveauction.model.Bid
import kotlinx.coroutines.flow.Flow

interface LiveAuctionRepository {
    fun getAuctionItemFlow(): Flow<List<AuctionItem>>
    suspend fun placeBid(itemId: String, bid: Bid): Result<Unit>
}