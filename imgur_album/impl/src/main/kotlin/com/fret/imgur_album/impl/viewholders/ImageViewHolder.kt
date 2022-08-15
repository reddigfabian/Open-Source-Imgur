package com.fret.imgur_album.impl.viewholders

import android.os.Build.VERSION.SDK_INT
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.fret.imgur_album.impl.R
import com.fret.imgur_album.impl.items.ImageListItem
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView


class ImageViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    private val itemImage = view.findViewById<ImageView>(R.id.ivItem)
    private val itemVideo = view.findViewById<StyledPlayerView>(R.id.pvItem)
    private val typeText = view.findViewById<TextView>(R.id.tvType)
    private val descriptionText = view.findViewById<TextView>(R.id.tvDescription)

    fun bind(item: ImageListItem) {
        when (item.type) {
            "video/mp4" -> {
                val simpleExoPlayer = ExoPlayer.Builder(itemVideo.context).build()
                itemVideo.player = simpleExoPlayer
                simpleExoPlayer.addMediaItem(MediaItem.fromUri(item.link.toUri()))
                simpleExoPlayer.prepare()
                simpleExoPlayer.playWhenReady = true
                itemImage.visibility = View.GONE
                itemVideo.visibility = View.VISIBLE
            }
            else -> {
                val imageLoader = ImageLoader.Builder(itemImage.context)
                    .components {
                        if (SDK_INT >= 28) {
                            add(ImageDecoderDecoder.Factory())
                        } else {
                            add(GifDecoder.Factory())
                        }
                    }
                    .build()
                itemImage.load(item.link, imageLoader)
                itemImage.visibility = View.VISIBLE
                itemVideo.visibility = View.GONE
            }
        }
        typeText.text = item.type
        item.description?.let {
            descriptionText.text = it
            descriptionText.visibility = View.VISIBLE
        } ?: run {
            descriptionText.visibility = View.GONE
        }
    }
}