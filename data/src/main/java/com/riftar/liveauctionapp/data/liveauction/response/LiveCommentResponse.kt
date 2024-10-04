package com.riftar.liveauctionapp.data.liveauction.response

import android.os.Build
import com.riftar.liveauctionapp.domain.liveauction.model.LiveComment
import java.util.Calendar

data class LiveCommentResponse(
    val id: Long = System.currentTimeMillis(),
    val author: String,
    val comment: String,
    val avatar: String = "https://picsum.photos/id/${
        Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + Build.VERSION.SDK_INT
    }/120/120",
    val timestamp: Long = System.currentTimeMillis()
) {
    constructor() : this(0, "", "", "", 0)

    companion object {
        fun LiveCommentResponse?.toDomainModel() = LiveComment(
            id = this?.id ?: 0,
            author = this?.author.orEmpty(),
            comment = this?.comment.orEmpty(),
            avatar = this?.avatar.orEmpty(),
            timestamp = this?.timestamp ?: 0
        )
    }
}