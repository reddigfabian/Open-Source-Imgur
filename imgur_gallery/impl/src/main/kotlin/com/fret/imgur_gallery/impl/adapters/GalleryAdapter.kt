package com.fret.imgur_gallery.impl.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.fret.gallery_list.impl.R
import com.fret.imgur_gallery.impl.items.GalleryItem
import com.fret.imgur_gallery.impl.viewholders.GalleryItemViewHolder

class GalleryAdapter(private val listener: GalleryItemClickListener) : PagingDataAdapter<GalleryItem, GalleryItemViewHolder>(
    ITEM_COMPARATOR
) {

    interface GalleryItemClickListener{
        fun onGalleryItemClick(item : GalleryItem)
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<GalleryItem>() {
            override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
                return oldItem.title == newItem.title &&
                        oldItem.views == newItem.views
            }
            override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryItemViewHolder {
        return GalleryItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_gallery, parent, false))
    }

    override fun onBindViewHolder(holder: GalleryItemViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
            holder.itemView.setOnClickListener {
                getItem(position)?.let { clickedItem ->
                    listener.onGalleryItemClick(clickedItem)
                }
            }
        }
    }

}