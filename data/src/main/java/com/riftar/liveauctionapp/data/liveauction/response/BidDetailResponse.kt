package com.riftar.liveauctionapp.data.liveauction.response

import android.os.Build
import com.riftar.liveauctionapp.domain.liveauction.model.BidDetail
import java.util.Calendar


data class BidDetailResponse(
    val userId: String,
    val amount: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val avatar: String = "https://picsum.photos/id/${
        Calendar.getInstance().get(Calendar.DAY_OF_YEAR)+ Build.VERSION.SDK_INT
    }/120/120"
) {
    constructor() : this(
        "",
        0.0,
        0,
        ""
    )
    companion object {
        fun BidDetailResponse?.toDomainModel() = BidDetail(
            userName = this?.userId.orEmpty(),
            amount = this?.amount ?: 0.0,
            timestamp = this?.timestamp ?: 0,
            avatar = this?.avatar.orEmpty()
        )
    }
}