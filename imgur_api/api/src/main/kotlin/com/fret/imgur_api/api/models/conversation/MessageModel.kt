package com.fret.imgur_api.api.models.conversation

/*
https://api.imgur.com/models/message

id	integer	The ID for the message
from	string	Account username of person sending the message
account_id	integer	The account ID of the person receiving the message
sender_id	integer	The account ID of the person who sent the message
body	string	Text of the message
conversation_id	integer	ID for the overall conversation
datetime	integer	Time message was sent, epoch time
 */
data class MessageModel(
    val id: Int,
    val from: String,
    val account_id: Int,
    val sender_id: Int,
    val body: String,
    val conversation_id: Int,
    val datetime: Int
)
