package com.fret.imgur_api.api.models.notification

import com.fret.imgur_api.api.models.comment.CommentModel
import com.fret.imgur_api.api.models.conversation.ConversationModel

/*
https://api.imgur.com/models/notification

id	integer	The ID for the notification
account_id	integer	The Account ID for the notification
viewed	boolean	Has the user viewed the image yet?
content	Mixed	This can be any other model, currently only using comments and conversation metadata.
 */
abstract class NotificationModel {
    abstract val id: Int
    abstract val account_id: Int
    abstract val view: Boolean
}

data class CommentNotificationModel(
    override val id: Int,
    override val account_id: Int,
    override val view: Boolean,
    val content: CommentModel
): NotificationModel()

data class ConversationNotificationModel(
    override val id: Int,
    override val account_id: Int,
    override val view: Boolean,
    val content: ConversationModel
): NotificationModel()