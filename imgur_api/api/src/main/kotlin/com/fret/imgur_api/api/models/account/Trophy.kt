package com.fret.imgur_api.api.models.account

/*
https://api.imgur.com/models/gallery_profile

id	integer	The ID of the trophy, this is unique to each trophy
name	string	The name of the trophy
name_clean	string	Can be thought of as the ID of a trophy type
description	string	A description of the trophy and how it was earned.
data	string	The ID of the image or the ID of the comment where the trophy was earned
data_link	string	A link to where the trophy was earned
datetime	integer	Date the trophy was earned, epoch time
image	string	URL of the image representing the trophy
 */
data class Trophy(
    val id: Int,
    val name: String,
    val name_clean: String,
    val description: String,
    val data: String,
    val data_link: String,
    val datetime: Int,
    val image: String
)
