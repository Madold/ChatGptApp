package com.markusw.chatgptapp.core.utils.ext

import androidx.compose.foundation.lazy.LazyListState

fun LazyListState.isLastItemVisible(): Boolean {
    val lastItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return lastItem == null || lastItem.size + lastItem.offset <= layoutInfo.viewportEndOffset
}