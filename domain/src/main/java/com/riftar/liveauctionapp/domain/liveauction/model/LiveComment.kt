package com.riftar.liveauctionapp.domain.liveauction.model

import android.os.Build
import java.util.Calendar

data class LiveComment(
    val id: Long = System.currentTimeMillis(),
    val author: String,
    val comment: String,
    val avatar: String = "https://picsum.photos/id/${
        Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + Build.VERSION.SDK_INT
    }/120/120",
    val timestamp: Long = System.currentTimeMillis()
) {
    constructor() : this(0, "", "", "", 0)
}