package com.riftar.liveauctionapp.domain.liveauction.repository

import com.riftar.liveauctionapp.domain.liveauction.model.LiveComment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun sendComment(comment: LiveComment)
    fun getComments(): Flow<List<LiveComment>>
}