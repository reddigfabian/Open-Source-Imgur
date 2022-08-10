package com.fret.gallery_list.impl.items

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fret.gallery_list.impl.R

class ImgurListItemViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    private val itemText = view.findViewById<TextView>(R.id.tvItem)
    private val itemImage = view.findViewById<ImageView>(R.id.ivItem)

    fun bind(item: ImgurListItem) {
        itemText.text = item.text
        itemImage.load(item.coverUrl)
    }
}