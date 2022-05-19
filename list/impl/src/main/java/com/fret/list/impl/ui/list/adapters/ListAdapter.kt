package com.fret.list.ui.list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.fret.list.R
import com.fret.list.impl.ui.list.items.ListItem
import com.fret.list.impl.ui.list.items.ListItemViewHolder

class ListAdapter(private val listener: ListItemClickListener) : PagingDataAdapter<ListItem, ListItemViewHolder>(ITEM_COMPARATOR) {

    interface ListItemClickListener{
        fun onItemClick(item : ListItem)
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<ListItem>() {
            override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean = oldItem.text == newItem.text
            override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        return ListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item)
            holder.itemView.setOnClickListener { clickedView ->
                getItem(position)?.let { clickedItem ->
                    listener.onItemClick(clickedItem)
                }
            }
        }
    }

}