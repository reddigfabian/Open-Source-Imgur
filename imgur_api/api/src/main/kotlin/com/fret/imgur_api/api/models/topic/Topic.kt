package com.fret.imgur_api.api.models.topic

import com.fret.imgur_api.api.models.gallery.GalleryItemModel
import com.fret.imgur_api.api.models.image.ImageModel

/*
https://api.imgur.com/models/topic

id	integer	ID of the topic.
name	string	Topic name
description	string	Description of the topic
css	string	CSS class used on website to style the ephemeral topic
ephemeral	boolean	Whether it is an ephemeral (e.g. current events) topic
topPost	Gallery Image OR Gallery Album	The top image in this topic
heroImage	Image	The current 'hero' image chosen by the Imgur community staff
isHero	boolean	Whether the topic's heroImage should be used as the overall hero image
 */
data class Topic (
    val id: Int,
    val name: String,
    val description: String,
    val css: String,
    val ephemeral: Boolean,
    val topPost: GalleryItemModel,
    val heroImage: ImageModel,
    val isHero: Boolean
)