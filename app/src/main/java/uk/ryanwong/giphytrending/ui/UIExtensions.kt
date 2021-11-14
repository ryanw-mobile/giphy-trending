package uk.ryanwong.giphytrending.ui

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import uk.ryanwong.giphytrending.R

fun SwipeRefreshLayout.setupRefreshLayout(
    refreshListener: SwipeRefreshLayout.OnRefreshListener
) {
    this.setColorSchemeColors(
        ContextCompat.getColor(context, R.color.design_default_color_primary),
        ContextCompat.getColor(context, R.color.design_default_color_primary_dark),
        ContextCompat.getColor(context, R.color.design_default_color_secondary)
    )
    this.setOnRefreshListener(refreshListener)
}

fun RecyclerView.setupRecyclerView(): RecyclerView {
    setHasFixedSize(false)
    itemAnimator = DefaultItemAnimator()
    addItemDecoration(
        DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
    )
    return this
}