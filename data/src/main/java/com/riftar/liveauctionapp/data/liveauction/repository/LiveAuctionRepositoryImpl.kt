package com.riftar.liveauctionapp.data.liveauction.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.riftar.liveauctionapp.data.liveauction.response.AuctionItemResponse
import com.riftar.liveauctionapp.data.liveauction.response.AuctionItemResponse.Companion.toDomainModel
import com.riftar.liveauctionapp.data.liveauction.response.BidDetailResponse
import com.riftar.liveauctionapp.data.liveauction.response.BidDetailResponse.Companion.toDomainModel
import com.riftar.liveauctionapp.domain.liveauction.model.AuctionItem
import com.riftar.liveauctionapp.domain.liveauction.model.BidDetail
import com.riftar.liveauctionapp.domain.liveauction.repository.LiveAuctionRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveAuctionRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase
) : LiveAuctionRepository {

    override fun getAuctionItemFlow(): Flow<List<AuctionItem>> = callbackFlow {
        val itemsRef = database.getReference("items")
        val listener = itemsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allItems = mutableListOf<AuctionItem>()
                for (child in snapshot.children) {
                    val auctionItem = child.getValue(AuctionItemResponse::class.java)
                    if (auctionItem != null) {
                        allItems.add(auctionItem.toDomainModel())
                    }
                }
                trySend(allItems)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { itemsRef.removeEventListener(listener) }
    }


    // in an ideal system, the timer should be retrieve from backend
    override fun getTimeRemaining(): Flow<Int> = callbackFlow {
        val timeRemainingRef = database.getReference("timeRemaining")
        val listener = timeRemainingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(Int::class.java) ?: 0)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { timeRemainingRef.removeEventListener(listener) }
    }

    override fun placeBid(itemId: String, bid: BidDetail) {
        val itemRef = database.getReference("items").child(itemId)
        val bidRef = database.getReference("bids")

        // reset time remaining
        database.getReference("timeRemaining").setValue(10)

        // save bid data to item
        itemRef.child("currentPrice").setValue(bid.amount)
        itemRef.child("currentBidder").setValue(bid.userName)

        // save bid data as history
        val bidId = System.currentTimeMillis().toString()
        bidRef.child(itemId).child(bidId).setValue(
            mapOf(
                "userId" to bid.userName,
                "amount" to bid.amount,
                "avatar" to bid.avatar,
                "timestamp" to System.currentTimeMillis()
            )
        )
    }

    override fun getBidHistory(itemId: String): Flow<List<BidDetail>> = callbackFlow {
        val bidsRef = database.getReference("bids").child(itemId)
        val listener = bidsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allBid = mutableListOf<BidDetail>()
                for (child in snapshot.children) {
                    val bidItem = child.getValue(BidDetailResponse::class.java)
                    if (bidItem != null) {
                        allBid.add(bidItem.toDomainModel())
                    }
                }
                trySend(allBid)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { bidsRef.removeEventListener(listener) }
    }
}