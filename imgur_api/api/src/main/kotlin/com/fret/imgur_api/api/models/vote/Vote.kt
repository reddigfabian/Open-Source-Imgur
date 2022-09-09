package com.fret.imgur_api.api.models.vote

/*
https://api.imgur.com/models/vote

ups	integer	Number of upvotes
downs	integer	Number of downvotes
 */
data class Vote(
    val ups: Int,
    val downs: Int
)
