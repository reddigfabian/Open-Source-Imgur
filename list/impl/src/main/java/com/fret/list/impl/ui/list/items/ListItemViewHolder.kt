package com.fret.list.impl.ui.list.items

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fret.list.R
import com.fret.list.impl.ui.list.items.ListItem

class ListItemViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    private val itemText = view.findViewById<TextView>(R.id.tvItem)

    fun bind(item: ListItem) {
        itemText.text = item.text
    }
}