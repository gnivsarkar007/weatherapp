package com.rtl.android.assignment.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rtl.android.assignment.R

enum class Errors {
    API, NETWORK
}

@Composable
fun ErrorScreen(state: Errors, onButtonClick: () -> Unit) {
    val triple = when (state) {
        Errors.API -> Triple(
            stringResource(R.string.something_went_wrong),
            stringResource(R.string.network_refresh_title),
            stringResource(R.string.refresh)
        )
        Errors.NETWORK -> Triple(
            stringResource(R.string.no_network),
            stringResource(R.string.check_network_title),
            stringResource(R.string.settings)
        )
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(id = R.drawable.ic_vector), contentDescription = "")
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = triple.first,
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = triple.second,
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = onButtonClick, modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colors.secondary,
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                Text(
                    triple.third,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.secondary)
                        .padding(8.dp)
                )
            }
        }
    }
}