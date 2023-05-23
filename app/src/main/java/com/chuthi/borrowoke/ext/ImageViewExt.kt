package com.chuthi.borrowoke.ext

import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.ImageLoader
import coil.decode.ImageDecoderDecoder
import coil.load


fun ImageView.loadImage(url: String? = null, @DrawableRes drawableRes: Int? = null) {
    try {
        // config image loader to playing .gif
        val imageLoader = ImageLoader.Builder(context)
            .components {
                add(ImageDecoderDecoder.Factory())
            }
            .build()

        load(data = url ?: drawableRes ?: return, imageLoader = imageLoader)
    } catch (ex: Exception) {
        // nothing
    }
}