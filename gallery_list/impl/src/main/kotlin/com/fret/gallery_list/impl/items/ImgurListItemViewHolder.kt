package com.fret.gallery_list.impl.items

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fret.gallery_list.impl.R

class ImgurListItemViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    private val itemText = view.findViewById<TextView>(R.id.tvItem)

    fun bind(item: ImgurListItem) {
        itemText.text = item.text
    }
}