package com.fret.gallery_list.impl.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fret.gallery_list.impl.R
import com.fret.gallery_list.impl.items.GalleryListItem

class GalleryListItemViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    private val itemImage = view.findViewById<ImageView>(R.id.ivItem)
    private val titleText = view.findViewById<TextView>(R.id.tvTitle)
    private val scoreText = view.findViewById<TextView>(R.id.tvScore)
    private val commentsText = view.findViewById<TextView>(R.id.tvComments)
    private val viewText = view.findViewById<TextView>(R.id.tvViews)

    fun bind(item: GalleryListItem) {
        itemImage.load(item.coverUrl)
        titleText.text = item.title
        scoreText.text = item.score.toString()
        commentsText.text = item.comments.toString()
        viewText.text = item.views.toString()
    }
}