package com.fret.imgur_api.api.models.comment

/*
https://api.imgur.com/models/comment

id	integer	The ID for the comment
image_id	string	The ID of the image that the comment is for
comment	string	The comment itself.
author	string	Username of the author of the comment
author_id	integer	The account ID for the author
on_album	boolean	If this comment was done to an album
album_cover	string	The ID of the album cover image, this is what should be displayed for album comments
ups	integer	Number of upvotes for the comment
downs	integer	The number of downvotes for the comment
points	float	the number of upvotes - downvotes
datetime	integer	Timestamp of creation, epoch time
parent_id	integer	If this is a reply, this will be the value of the comment_id for the caption this a reply for.
deleted	boolean	Marked true if this caption has been deleted
vote	string	The current user's vote on the comment. null if not signed in or if the user hasn't voted on it.
children	Array of comments	All of the replies for this comment. If there are no replies to the comment then this is an empty set.
 */
data class CommentModel(
    val id: Int,
    val image_id: String,
    val comment: String,
    val author: String,
    val author_id: String,
    val on_album: String,
    val album_cover: String,
    val ups: Int,
    val downs: Int,
    val points: Float,
    val dateTime: Int,
    val parent_id: Int,
    val deleted: Boolean,
    val vote: String?,
    val children: List<CommentModel>
)
