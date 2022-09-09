package com.fret.imgur_api.api.models.account

import com.squareup.moshi.JsonClass

/*
https://api.imgur.com/models/account_settings

account_url	string	The account username
email	string	The users email address
public_images	boolean	Automatically allow all images to be publicly accessible
pro_expiration	integer or boolean	False if not a pro user, their expiration date if they are.
accepted_gallery_terms	boolean	True if the user has accepted the terms of uploading to the Imgur gallery.
active_emails	Array of Strings	The email addresses that have been activated to allow uploading
messaging_enabled	boolean	If the user is accepting incoming messages or not
blocked_users	Array of objects	An array of users that have been blocked from messaging, the object is blocked_id and blocked_url.
show_mature	boolean	True if the user has opted to have mature images displayed in gallery list endpoints.
first_party	boolean	True unless the user created their account via a third party service such as Google Plus.
 */
abstract class AccountSettingsModel {
    abstract val account_url: String
    abstract val email: String
    abstract val public_images: Boolean
    abstract val accepted_gallery_terms: Boolean
    abstract val active_emails: List<String>
    abstract val messaging_enabled: Boolean
    abstract val blocked_users: List<BlockedUserModel>
    abstract val show_mature: Boolean
    abstract val first_party: Boolean
}

@JsonClass(generateAdapter = true)
data class NonProAccountSettingsModel(
    override val account_url: String,
    override val email: String,
    override val public_images: Boolean,
    val pro_expiration: Boolean,
    override val accepted_gallery_terms: Boolean,
    override val active_emails: List<String>,
    override val messaging_enabled: Boolean,
    override val blocked_users: List<BlockedUserModel>,
    override val show_mature: Boolean,
    override val first_party: Boolean
): AccountSettingsModel()

@JsonClass(generateAdapter = true)
data class ProAccountSettingsModel(
    override val account_url: String,
    override val email: String,
    override val public_images: Boolean,
    val pro_expiration: Integer,
    override val accepted_gallery_terms: Boolean,
    override val active_emails: List<String>,
    override val messaging_enabled: Boolean,
    override val blocked_users: List<BlockedUserModel>,
    override val show_mature: Boolean,
    override val first_party: Boolean
): AccountSettingsModel()
