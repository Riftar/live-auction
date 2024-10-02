package com.riftar.liveauctionapp.data.liveauction.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.riftar.liveauctionapp.domain.liveauction.model.AuctionItem
import com.riftar.liveauctionapp.domain.liveauction.model.Bid
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


object FirebaseHelper {
    private val database = FirebaseDatabase.getInstance().reference

    fun getAuctionItemFlow(itemId: String): Flow<AuctionItem> = callbackFlow {
        val listener = database.child("items").child(itemId).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val item = snapshot.getValue(AuctionItem::class.java)
                    item?.let { trySend(it) }
                }
                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }
        )
        awaitClose { database.child("items").child(itemId).removeEventListener(listener) }
    }

    suspend fun placeBid(itemId: String, bid: Bid) {
        database.child("items").child(itemId).child("currentPrice").setValue(bid.amount)
        database.child("items").child(itemId).child("currentBidder").setValue(bid.userId)
        database.child("items").child(itemId).child("timeRemaining").setValue(10)
        database.child("bids").child(itemId).push().setValue(bid)
    }
}