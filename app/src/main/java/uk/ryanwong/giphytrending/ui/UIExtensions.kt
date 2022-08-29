package uk.ryanwong.giphytrending.ui

import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

fun View.animateDown() {
    val animate = TranslateAnimation(
        0F,
        0F,
        -1 * this.getHeight().toFloat(),
        0F
    ).apply {
        duration = 1000
        fillAfter = true
    }
    startAnimation(animate)
}

fun View.animateUp() {
    val animate = TranslateAnimation(
        0F,
        0F,
        0F,
        -1 * this.getHeight().toFloat()
    ).apply {
        duration = 500
        fillAfter = true
        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                this@animateUp.visibility = View.GONE
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
    }
    startAnimation(animate)
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
