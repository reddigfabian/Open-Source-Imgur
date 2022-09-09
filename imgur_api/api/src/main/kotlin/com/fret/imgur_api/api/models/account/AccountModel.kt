package com.fret.imgur_api.api.models.account

import com.squareup.moshi.JsonClass

/*
https://api.imgur.com/models/account

id	Integer	The account id for the username requested.
url	String	The account username, will be the same as requested in the URL
bio	String	A basic description the user has filled out
reputation	Float	The reputation for the account, in it's numerical format.
created	Integer	The epoch time of account creation
pro_expiration	Integer or Boolean	False if not a pro user, their expiration date if they are.
 */
abstract class AccountModel {
    abstract val id: Int
    abstract val url: String
    abstract val bio: String?
    abstract val reputation: Float
    abstract val created: Int
}

@JsonClass(generateAdapter = true)
data class NonProAccountModel(
    override val id: Int,
    override val url: String,
    override val bio: String?,
    override val reputation: Float,
    override val created: Int,
    val pro_expiration: Boolean
) : AccountModel()

@JsonClass(generateAdapter = true)
data class ProAccountModel(
    override val id: Int,
    override val url: String,
    override val bio: String?,
    override val reputation: Float,
    override val created: Int,
    val pro_expiration: Int
) : AccountModel()
