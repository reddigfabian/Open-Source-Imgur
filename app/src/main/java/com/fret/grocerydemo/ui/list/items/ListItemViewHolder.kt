package com.fret.grocerydemo.ui.list.items

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fret.grocerydemo.R

class ListItemViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    private val itemText = view.findViewById<TextView>(R.id.tvItem)

    fun bind(item: ListItem) {
        itemText.text = item.text
    }
}