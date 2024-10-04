package com.riftar.liveauctionapp.domain.liveauction.repository

import com.riftar.liveauctionapp.domain.liveauction.model.AuctionItem
import com.riftar.liveauctionapp.domain.liveauction.model.BidDetail
import com.riftar.liveauctionapp.domain.liveauction.model.LiveComment
import kotlinx.coroutines.flow.Flow

interface LiveAuctionRepository {
    fun getAuctionItemFlow(): Flow<List<AuctionItem>>
    fun getTimeRemaining(): Flow<Int>
    fun placeBid(itemId: String, bid: BidDetail)
    fun getBidHistory(itemId: String): Flow<List<BidDetail>>
}