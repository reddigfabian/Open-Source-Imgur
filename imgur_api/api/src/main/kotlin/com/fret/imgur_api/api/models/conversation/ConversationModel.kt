package com.fret.imgur_api.api.models.conversation

/*
https://api.imgur.com/models/conversation

id	integer	Conversation ID
last_message_preview	string	Preview of the last message
datetime	integer	Time of last sent message, epoch time
with_account_id	integer	Account ID of the other user in conversation
with_account	string	Account username of the other user in conversation
message_count	integer	Total number of messages in the conversation
messages	Array of Messages	OPTIONAL: (only available when requesting a specific conversation) Reverse sorted such that most recent message is at the end of the array.
done	boolean	OPTIONAL: (only available when requesting a specific conversation) Flag to indicate whether you've reached the beginning of the thread.
page	integer	OPTIONAL: (only available when requesting a specific conversation) Number of the next page
 */
data class ConversationModel(
    val id: Int,
    val last_message_preview: String,
    val datetime: Int,
    val with_account_id: Int,
    val with_account: String,
    val message_count: Int,
    val messageModels: List<MessageModel>?,
    val done: Boolean?,
    val page: Int?
)
