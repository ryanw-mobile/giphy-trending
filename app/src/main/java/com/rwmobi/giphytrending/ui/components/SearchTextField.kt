/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.rwmobi.giphytrending.R
import com.rwmobi.giphytrending.ui.theme.GiphyTrendingTheme
import kotlinx.coroutines.launch

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    keyword: String,
    focusManager: FocusManager,
    onUpdateKeyword: (keyword: String) -> Unit,
    onClearKeyword: () -> Unit,
    onSearch: () -> Unit,
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    var isFocused by remember { mutableStateOf(false) }

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = GiphyTrendingTheme.dimens.defaultHalfPadding)
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .semantics {
                contentDescription = context.getString(R.string.content_description_search_bar)
            },
        value = keyword,
        onValueChange = { onUpdateKeyword(it) },
        singleLine = true,
        maxLines = 1,
        shape = GiphyTrendingTheme.shapes.medium,
        colors = TextFieldDefaults.colors().copy(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = GiphyTrendingTheme.colorScheme.onPrimaryContainer.copy(
                alpha = 0.28f,
            ),
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
                coroutineScope.launch {
                    focusManager.clearFocus()
                }
            },
        ),
        placeholder = {
            Text(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                maxLines = 1,
                style = GiphyTrendingTheme.typography.bodyMedium,
                text = stringResource(id = R.string.search),
            )
        },
        leadingIcon = {
            Icon(
                modifier = Modifier.wrapContentSize(),
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = GiphyTrendingTheme.colorScheme.onPrimaryContainer.copy(
                    alpha = 0.28f,
                ),
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = isFocused && keyword.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                IconButton(onClick = onClearKeyword) {
                    Icon(
                        modifier = Modifier.wrapContentSize(),
                        imageVector = Icons.Filled.Clear,
                        contentDescription = stringResource(R.string.content_description_clear_search_keyword),
                        tint = GiphyTrendingTheme.colorScheme.onPrimaryContainer.copy(
                            alpha = 0.68f,
                        ),
                    )
                }
            }
        },
    )
}

@Preview
@Composable
private fun SearchTextFieldEmptyPreview() {
    GiphyTrendingTheme {
        Surface {
            SearchTextField(
                keyword = "",
                focusManager = LocalFocusManager.current,
                onUpdateKeyword = {},
                onClearKeyword = {},
                onSearch = { },
            )
        }
    }
}

@Preview
@Composable
private fun SearchTextFieldPreview() {
    GiphyTrendingTheme {
        Surface {
            SearchTextField(
                keyword = "some keyword which is pretty long but still within the limit",
                focusManager = LocalFocusManager.current,
                onUpdateKeyword = {},
                onClearKeyword = {},
                onSearch = { },
            )
        }
    }
}
