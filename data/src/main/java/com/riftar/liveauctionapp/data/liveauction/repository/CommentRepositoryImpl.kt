package com.riftar.liveauctionapp.data.liveauction.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.riftar.liveauctionapp.data.liveauction.response.LiveCommentResponse
import com.riftar.liveauctionapp.data.liveauction.response.LiveCommentResponse.Companion.toDomainModel
import com.riftar.liveauctionapp.domain.liveauction.model.LiveComment
import com.riftar.liveauctionapp.domain.liveauction.repository.CommentRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase
) : CommentRepository {
    override fun sendComment(comment: LiveComment) {
        val commentRef = database.getReference("comments")
        // save comment data
        val bidId = System.currentTimeMillis().toString()
        commentRef.child(bidId).setValue(
            mapOf(
                "id" to comment.id,
                "author" to comment.author,
                "avatar" to comment.avatar,
                "comment" to comment.comment,
                "timestamp" to System.currentTimeMillis()
            )
        )
    }

    override fun getComments(): Flow<List<LiveComment>> = callbackFlow {
        val bidsRef = database.getReference("comments")
        val listener = bidsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listComment = mutableListOf<LiveComment>()
                for (child in snapshot.children) {
                    val comment = child.getValue(LiveCommentResponse::class.java)
                    if (comment != null) {
                        listComment.add(comment.toDomainModel())
                    }
                }
                trySend(listComment)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { bidsRef.removeEventListener(listener) }
    }
}