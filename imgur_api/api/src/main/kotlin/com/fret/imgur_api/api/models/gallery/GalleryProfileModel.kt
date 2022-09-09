package com.fret.imgur_api.api.models.gallery

import com.fret.imgur_api.api.models.account.Trophy

/*
https://api.imgur.com/models/gallery_profile

total_gallery_comments	integer	Total number of comments the user has made in the gallery
total_gallery_favorites	integer	Total number of items favorited by the user in the gallery
total_gallery_submissions	integer	Total number of images submitted by the user.
trophies	Array	An array of trophies that the user has.
 */
data class GalleryProfileModel (
    val total_gallery_comments: Int,
    val total_gallery_favorites: Int,
    val total_gallery_submissions: Int,
    val trophies: List<Trophy>
)