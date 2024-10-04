package com.riftar.liveauctionapp.liveauction.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StarterText(modifier: Modifier, startingCountDownTimer: Int) {
    if (startingCountDownTimer > 0) {
        Column(
            modifier = modifier
                .background(
                    color = Color.DarkGray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Starting In",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = startingCountDownTimer.toString(),
                style = MaterialTheme.typography.headlineLarge.copy(color = Color.White)
            )
        }
    }
}