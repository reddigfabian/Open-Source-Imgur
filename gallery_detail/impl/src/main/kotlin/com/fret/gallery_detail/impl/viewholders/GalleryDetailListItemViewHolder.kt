package com.fret.gallery_detail.impl.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fret.gallery_detail.impl.R
import com.fret.gallery_detail.impl.items.GalleryDetailListItem

class GalleryDetailListItemViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    private val itemImage = view.findViewById<ImageView>(R.id.ivItem)
    private val descriptionText = view.findViewById<TextView>(R.id.tvDescription)

    fun bind(item: GalleryDetailListItem) {
        itemImage.load(item.link)
        item.description?.let {
            descriptionText.text = it
            descriptionText.visibility = View.VISIBLE
        } ?: run {
            descriptionText.visibility = View.GONE
        }
    }
}