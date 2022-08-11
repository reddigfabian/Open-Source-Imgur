package com.fret.imgur_api.api.models.album

/*
https://api.imgur.com/models/image

id	string	The ID for the image
title	string	The title of the image.
description	string	Description of the image.
datetime	integer	Time uploaded, epoch time
type	string	Image MIME type.
animated	boolean	is the image animated
width	integer	The width of the image in pixels
height	integer	The height of the image in pixels
size	integer	The size of the image in bytes
views	integer	The number of image views
bandwidth	integer	Bandwidth consumed by the image in bytes
deletehash	string	OPTIONAL, the deletehash, if you're logged in as the image owner
name	string	OPTIONAL, the original filename, if you're logged in as the image owner
section	string	If the image has been categorized by our backend then this will contain the section the image belongs in. (funny, cats, adviceanimals, wtf, etc)
link	string	The direct link to the the image. (Note: if fetching an animated GIF that was over 20MB in original size, a .gif thumbnail will be returned)
gifv	string	OPTIONAL, The .gifv link. Only available if the image is animated and type is 'image/gif'.
mp4	string	OPTIONAL, The direct link to the .mp4. Only available if the image is animated and type is 'image/gif'.
mp4_size	integer	OPTIONAL, The Content-Length of the .mp4. Only available if the image is animated and type is 'image/gif'. Note that a zero value (0) is possible if the video has not yet been generated
looping	boolean	OPTIONAL, Whether the image has a looping animation. Only available if the image is animated and type is 'image/gif'.
favorite	boolean	Indicates if the current user favorited the image. Defaults to false if not signed in.
nsfw	boolean	Indicates if the image has been marked as nsfw or not. Defaults to null if information is not available.
vote	string	The current user's vote on the album. null if not signed in, if the user hasn't voted on it, or if not submitted to the gallery.
in_gallery	boolean	True if the image has been submitted to the gallery, false if otherwise.
 */
data class ImageModel(
    val id: String,
    val title : String?,
    val description: String?,
    val datetime: Int,
    val type: String,
    val animated: Boolean? = false,
    val width: Int,
    val height: Int,
    val size: Int,
    val views: Int,
    val deleteHash: String?,
    val name: String?,
    val section: String?,
    val link: String?,
    val gifv: String?,
    val mp4: String?,
    val mp4_size: Int?,
    val looping: Boolean? = false,
    val favorite: Boolean? = false,
    val nsfw: Boolean? = false,
    val vote: String?,
    val in_gallery: Boolean? = false

)