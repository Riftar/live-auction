package com.riftar.liveauctionapp.data.liveauction.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.riftar.liveauctionapp.domain.liveauction.model.AuctionItem
import com.riftar.liveauctionapp.domain.liveauction.model.Bid
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
                val allItems2 = mutableListOf<AuctionItem>()
                Log.d("Rifqi-test", "Children is: ${snapshot.children}")
                for (child in snapshot.children) {
                    //todo create dedicated AuctionItemResponse class that accept null
                    val auctionItem = child.getValue(AuctionItem::class.java)
                    if (auctionItem != null) {
                        allItems2.add(auctionItem)
                    }
                }
                Log.d("Rifqi-test", "Wrap is: $allItems2")
                val items = snapshot.children.mapNotNull { it.getValue(AuctionItem::class.java) }

                trySend(allItems2)
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

    override suspend fun placeBid(itemId: String, bid: Bid) {
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
                "timestamp" to System.currentTimeMillis()
            )
        )
    }
}