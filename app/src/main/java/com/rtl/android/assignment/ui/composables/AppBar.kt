package com.rtl.android.assignment.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rtl.android.assignment.R

@Composable
fun AppBar(isDetails: Boolean, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        val alignment = when (isDetails) {
            false -> TextAlign.Center
            else -> TextAlign.Start
        }
        val bg = if (isDetails) Color.Transparent else MaterialTheme.colors.background
        Text(
            stringResource(R.string.top_bar_title),
            textAlign = alignment,
            style = MaterialTheme.typography.h2,
            modifier = Modifier
                .background(bg)
                .fillMaxSize()
                .padding(8.dp)
        )
    }
}