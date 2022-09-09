package com.fret.imgur_api.api.models.tag

import com.fret.imgur_api.api.models.gallery.GalleryItemModel

/*
https://api.imgur.com/models/tag

name	string	Name of the tag.
followers	integer	Number of followers for the tag.
total_items	integer	Total number of gallery items for the tag.
following	boolean	OPTIONAL, boolean representing whether or not the user is following the tag in their custom gallery
items	Array of Gallery Images and Gallery Albums	Gallery items with current tag.
 */
data class Tag(
    val name: String,
    val followers: Int,
    val total_items: Int,
    val following: Boolean?,
    val items: List<GalleryItemModel>
)
