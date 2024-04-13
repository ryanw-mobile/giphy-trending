/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme
import com.rwmobi.giphytrending.ui.theme.getDimension

@Composable
fun NoDataScreen(
    modifier: Modifier = Modifier,
) {
    val dimension = LocalConfiguration.current.getDimension()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .size(size = 64.dp),
            painter = painterResource(id = R.drawable.outline_no_photography_24),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.68f)),
        )

        Spacer(modifier = Modifier.height(height = dimension.defaultFullPadding))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimension.defaultHalfPadding),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            text = stringResource(R.string.there_is_nothing_to_show_try_pull_to_reload),
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun NoDataScreenPreview() {
    GiphyTrendingTheme {
        Surface {
            NoDataScreen(modifier = Modifier.fillMaxSize())
        }
    }
}
