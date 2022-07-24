package com.rtl.android.assignment.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rtl.android.assignment.R

@Composable
fun AddCityButton(opnSearch: () -> Unit) {
    FloatingActionButton(onClick = { opnSearch() }) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(MaterialTheme.colors.secondary, shape = RoundedCornerShape(4.dp))
                .padding(8.dp)
                .wrapContentSize()
        ) {
            Image(painter = painterResource(R.drawable.ic_add), contentDescription = "")
        }
    }
}