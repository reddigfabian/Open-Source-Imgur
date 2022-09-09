package com.fret.imgur_api.api.models.gallery

data class GalleryItemModel(
    val id: String,
    val title : String?,
    val cover: String?,
    val score: Int = 0,
    val comment_count: Int = 0,
    val views: Int = 0
)

/*
id	string	The ID for the image
title	string	The title of the album in the gallery
description	string	The description of the album in the gallery
datetime	integer	Time inserted into the gallery, epoch time
cover	string	The ID of the album cover image
cover_width	integer	The width, in pixels, of the album cover image
cover_height	integer	The height, in pixels, of the album cover image
account_url	string	The account username or null if it's anonymous.
account_id	integer	The account ID of the account that uploaded it, or null.
privacy	string	The privacy level of the album, you can only view public if not logged in as album owner
layout	string	The view layout of the album.
views	integer	The number of image views
link	string	The URL link to the album
ups	integer	Upvotes for the image
downs	integer	Number of downvotes for the image
points	integer	Upvotes minus downvotes
score	integer	Imgur popularity score
is_album	boolean	if it's an album or not
vote	string	The current user's vote on the album. null if not signed in or if the user hasn't voted on it.
favorite	boolean	Indicates if the current user favorited the album. Defaults to false if not signed in.
nsfw	boolean	Indicates if the album has been marked as nsfw or not. Defaults to null if information is not available.
comment_count	int	Number of comments on the gallery album.
topic	string	Topic of the gallery album.
topic_id	integer	Topic ID of the gallery album.
images_count	integer	The total number of images in the album
images	Array of Images	An array of all the images in the album (only available when requesting the direct album)
in_most_viral	boolean	Indicates if the album is in the most viral gallery or not.
 */
//data class GalleryAlbumModel(
//
//)