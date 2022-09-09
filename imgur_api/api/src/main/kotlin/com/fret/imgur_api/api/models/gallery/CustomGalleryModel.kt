package com.fret.imgur_api.api.models.gallery

/*
https://api.imgur.com/models/custom_gallery

account_url	string	Username of the account that created the custom gallery
link	string	The URL link to the custom gallery
tags	array	An array of all the tag names in the custom gallery
item_count	integer	The total number of gallery items in the custom gallery
items	Array of Gallery Images and Gallery Albums	An array of all the gallery items in the custom gallery
 */
data class CustomGalleryModel(
    val account_url: String,
    val link: String,
    val tags: List<String>,
    val item_count: Int,
    val items: List<GalleryItemModel>
)
