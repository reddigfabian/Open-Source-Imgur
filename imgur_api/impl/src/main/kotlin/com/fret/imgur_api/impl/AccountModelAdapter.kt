package com.fret.imgur_api.impl

import com.fret.imgur_api.api.models.account.AccountModel
import com.fret.imgur_api.api.models.account.NonProAccountModel
import com.fret.imgur_api.api.models.account.ProAccountModel
import com.squareup.moshi.*

class AccountModelAdapter {
    @FromJson
    fun fromJson(jsonReader: JsonReader, nonProDelegate: JsonAdapter<NonProAccountModel>, proDelegate: JsonAdapter<ProAccountModel>): AccountModel? {
        return try {
            nonProDelegate.fromJson(jsonReader)
        } catch (ex: JsonDataException) {
            try {
                proDelegate.fromJson(jsonReader)
            } catch (ex: JsonDataException) {
                null
            }
        }
    }

    @ToJson
    fun toJson(jsonWriter: JsonWriter, accountModel: AccountModel, proDelegate: JsonAdapter<ProAccountModel>, nonProDelegate: JsonAdapter<NonProAccountModel>) {
        when (accountModel) {
            is ProAccountModel -> proDelegate.toJson(jsonWriter, accountModel)
            is NonProAccountModel -> nonProDelegate.toJson(jsonWriter, accountModel)
        }
    }
}