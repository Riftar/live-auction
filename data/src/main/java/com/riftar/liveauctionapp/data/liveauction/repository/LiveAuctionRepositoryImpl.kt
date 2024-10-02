package com.riftar.liveauctionapp.data.liveauction.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
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

                val value = snapshot.value
//                // Assuming you have the JSON string
//                val gson = Gson()
//
//                // Accessing specific item by key:
//                val item1Data = gson.fromJson(value.toString(), AuctionItem::class.java)
//
//                // Accessing all items (requires further processing):
//                val itemsMap =
//                    gson.fromJson(value.toString(), Map::class.java) as Map<String, Map<String, Any>>
//                val allItems = itemsMap["items"] as Map<String, Any>
//                // You can then iterate through the "allItems" map to access individual items
//
//                Log.d("Rifqi-test", "Snapshot is: ${snapshot.value}")
//                Log.d("Rifqi-test", "Value is: $value")
//                Log.d("Rifqi-test", "Wrap is: $allItems")
//                Log.d("Rifqi-test", "Item 1 is: $item1Data")


                val allItems2 = mutableListOf<AuctionItem>()

                for (child in snapshot.children) {
                    val itemMap = child.value as Map<String, Any>
                    val auctionItem = AuctionItem(
                        itemMap["id"] as String,
                        itemMap["name"] as String,
                        itemMap["description"] as String,
                        itemMap["imageUrl"] as String,
                        // check how to save double on firebase
                        (itemMap["basePrice"] as Long).toInt(), // Assuming price is a double
                        (itemMap["currentPrice"] as Long).toInt(),
                        itemMap["currentBidder"] as String,
                        (itemMap["timeRemaining"] as Long).toInt() // Convert timeRemaining to Int
                    )
                    allItems2.add(auctionItem)
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

    override suspend fun placeBid(itemId: String, bid: Bid): Result<Unit> {
//        return withContext(Dispatchers.IO) {
//            try {
//                val itemRef = database.getReference("items").child(itemId)
//                val bidRef = database.getReference("bids").child(itemId).push()
//
//                val transactionResult = database.reference.runTransaction(object : Transaction.Handler {
//                    override fun doTransaction(mutableData: MutableData): Transaction.Result {
//                        val item = mutableData.child("items").child(itemId).getValue(AuctionItem::class.java)
//                        if (item != null && bid.amount > item.currentPrice) {
//                            mutableData.child("items").child(itemId).child("currentPrice").value = bid.amount
//                            mutableData.child("items").child(itemId).child("currentBidder").value = bid.userId
//                            mutableData.child("bids").child(itemId).child(bidRef.key!!).value = mapOf(
//                                "userId" to bid.userId,
//                                "amount" to bid.amount,
//                                "timestamp" to ServerValue.TIMESTAMP
//                            )
//                            return Transaction.success(mutableData)
//                        }
//                        return Transaction.abort()
//                    }
//
//                    override fun onComplete(
//                        error: DatabaseError?,
//                        committed: Boolean,
//                        currentData: DataSnapshot?
//                    ) {
//                        // This method is called even if the transaction was aborted,
//                        // so we don't need to do anything here.
//                    }
//                })
//
//                if (transactionResult.isSuccessful) {
//                    Result.success(Unit)
//                } else {
//                    Result.failure(Exception("Bid not placed: Transaction failed or was aborted"))
//                }
//            } catch (e: Exception) {
//                Result.failure(e)
//            }
//        }
        return Result.success(Unit)
    }
}