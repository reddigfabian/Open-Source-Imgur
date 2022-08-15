package com.fret.imgur_album.impl.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fret.imgur_album.impl.R
import com.fret.imgur_album.impl.items.ImageListItem
import com.fret.imgur_album.impl.viewholders.ImageViewHolder

class AlbumAdapter: ListAdapter<ImageListItem, ImageViewHolder>(
    ITEM_COMPARATOR
) {
    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<ImageListItem>() {
            override fun areContentsTheSame(oldItem: ImageListItem, newItem: ImageListItem): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areItemsTheSame(oldItem: ImageListItem, newItem: ImageListItem): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_imgur_album, parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}