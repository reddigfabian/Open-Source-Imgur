package com.fret.gallery_list.impl.adapters

import com.fret.gallery_list.impl.items.GalleryListItem
import com.fret.gallery_list.impl.viewholders.GalleryListItemViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.fret.gallery_list.impl.R

class GalleryListAdapter(private val listener: GalleryListItemClickListener) : PagingDataAdapter<GalleryListItem, GalleryListItemViewHolder>(
    ITEM_COMPARATOR
) {

    interface GalleryListItemClickListener{
        fun onGalleryItemClick(item : GalleryListItem)
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<GalleryListItem>() {
            override fun areContentsTheSame(oldItem: GalleryListItem, newItem: GalleryListItem): Boolean {
                return oldItem.title == newItem.title &&
                        oldItem.views == newItem.views
            }
            override fun areItemsTheSame(oldItem: GalleryListItem, newItem: GalleryListItem): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryListItemViewHolder {
        return GalleryListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_gallery, parent, false))
    }

    override fun onBindViewHolder(holder: GalleryListItemViewHolder, position: Int) {
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