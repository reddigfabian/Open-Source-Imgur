package com.fret.gallery_detail.impl.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fret.gallery_detail.impl.R
import com.fret.gallery_detail.impl.items.GalleryDetailListItem
import com.fret.gallery_detail.impl.viewholders.GalleryDetailListItemViewHolder

class GalleryDetailAdapter: ListAdapter<GalleryDetailListItem, GalleryDetailListItemViewHolder>(
    ITEM_COMPARATOR
) {
    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<GalleryDetailListItem>() {
            override fun areContentsTheSame(oldItem: GalleryDetailListItem, newItem: GalleryDetailListItem): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areItemsTheSame(oldItem: GalleryDetailListItem, newItem: GalleryDetailListItem): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryDetailListItemViewHolder {
        return GalleryDetailListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_gallery_detail, parent, false))
    }

    override fun onBindViewHolder(holder: GalleryDetailListItemViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

}