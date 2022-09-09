package com.fret.imgur_api.api.models.tag

/*
https://api.imgur.com/models/tag_vote

ups	integer	Number of upvotes.
downs	integer	Number of downvotes.
name	string	Name of the tag.
author	string	Author of the tag.
 */
data class TagVote(
    val ups: Int,
    val downs: Int,
    val name: String,
    val author: String
)
