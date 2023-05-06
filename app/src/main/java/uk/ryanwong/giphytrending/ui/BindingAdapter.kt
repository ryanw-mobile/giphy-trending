package uk.ryanwong.giphytrending.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import uk.ryanwong.giphytrending.R

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, src: String?) {
    src?.let {
        val uri = src.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView).load(uri)
//            .placeholder(R.drawable.ic_baseline_error_outline_24)
//            .error(R.drawable.ic_baseline_error_outline_24)
//            .fallback(R.drawable.ic_baseline_error_outline_24)
            .into(imageView)
    } ?: run {
        // src is null, we still have to show a placeholder
//        imageView.setImageResource(R.drawable.ic_baseline_error_outline_24)
    }
}

@BindingAdapter("onClickToOpen")
fun setBrowserIntentClickable(view: View, src: String?) {
    src?.let {
        val uri = src.toUri().buildUpon().scheme("https").build()
        view.visibility = View.VISIBLE
        view.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).setData(uri)
            view.context.startActivity(intent)
        }
    } ?: run {
        // If the link is empty we would better hide this
        view.visibility = View.GONE
    }
}

@BindingAdapter("onClickToShare")
fun setClipboardClickable(view: View, src: String?) {
    src?.let {
        val uri = src.toUri().buildUpon().scheme("https").build()
        view.visibility = View.VISIBLE
        view.setOnClickListener {
            val clipboard: ClipboardManager? =
                view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText(uri.toString(), uri.toString())
            clipboard?.setPrimaryClip(clip).also {
                val snackbar = Snackbar.make(
                    view,
                    view.context.getString(R.string.clipboard_copied),
                    Snackbar.LENGTH_LONG,
                )
                val layoutParams =
                    (snackbar.view.layoutParams as CoordinatorLayout.LayoutParams).apply {
                        anchorId = R.id.nav_view // Id for your bottomNavBar or TabLayout
                        anchorGravity = Gravity.TOP
                        gravity = Gravity.TOP
                    }
                snackbar.view.layoutParams = layoutParams
                snackbar.show()
            } ?: run {
                val snackbar = Snackbar.make(
                    view,
                    view.context.getString(R.string.error_export_clipboard),
                    Snackbar.LENGTH_LONG,
                )
                val layoutParams =
                    (snackbar.view.layoutParams as CoordinatorLayout.LayoutParams).apply {
                        anchorId = R.id.nav_view // Id for your bottomNavBar or TabLayout
                        anchorGravity = Gravity.TOP
                        gravity = Gravity.TOP
                    }
                snackbar.view.layoutParams = layoutParams
                snackbar.show()
            }
        }
    } ?: run {
        // If the link is empty we would better hide this
        view.visibility = View.GONE
    }
}
