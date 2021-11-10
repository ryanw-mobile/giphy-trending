package uk.ryanwong.giphytrending.ui

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, src: String?) {
    src?.let {
        val uri = src.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView)
            .load(uri)
//            .placeholder(R.drawable.ic_baseline_error_outline_24)
//            .error(R.drawable.ic_baseline_error_outline_24)
//            .fallback(R.drawable.ic_baseline_error_outline_24)
            .into(imageView)
    } ?: run {
        // src is null, we still have to show a placeholder
//        imageView.setImageResource(R.drawable.ic_baseline_error_outline_24)
    }
}