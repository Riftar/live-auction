package com.riftar.liveauctionapp.domain.liveauction.model

import android.os.Build
import java.util.Calendar

data class BidDetail(
    val userName: String,
    val amount: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val avatar: String = "https://picsum.photos/id/${
        Calendar.getInstance().get(Calendar.DAY_OF_YEAR)+ Build.VERSION.SDK_INT
    }/120/120"
)