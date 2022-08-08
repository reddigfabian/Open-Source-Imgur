package com.fret.gallery_list.impl.adapters

import com.fret.gallery_list.impl.items.ImgurListItem
import com.fret.gallery_list.impl.items.ImgurListItemViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.fret.gallery_list.impl.R

class ImgurListAdapter(private val listener: ImgurListItemClickListener) : PagingDataAdapter<ImgurListItem, ImgurListItemViewHolder>(
    ITEM_COMPARATOR
) {

    interface ImgurListItemClickListener{
        fun onImgurItemClick(item : ImgurListItem)
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<ImgurListItem>() {
            override fun areContentsTheSame(oldItem: ImgurListItem, newItem: ImgurListItem): Boolean = oldItem.text == newItem.text
            override fun areItemsTheSame(oldItem: ImgurListItem, newItem: ImgurListItem): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgurListItemViewHolder {
        return ImgurListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun onBindViewHolder(holder: ImgurListItemViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
            holder.itemView.setOnClickListener { clickedView ->
                getItem(position)?.let { clickedItem ->
                    listener.onImgurItemClick(clickedItem)
                }
            }
        }
    }

}