package com.fret.grocerydemo.ui.list.adapters

import com.fret.grocerydemo.ui.list.items.ListItem
import com.fret.grocerydemo.ui.list.items.ListItemViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.fret.grocerydemo.R

class ListAdapter : PagingDataAdapter<ListItem, ListItemViewHolder>(ITEM_COMPARATOR) {

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
        getItem(position)?.let {
            holder.bind(it)
        }
    }

}