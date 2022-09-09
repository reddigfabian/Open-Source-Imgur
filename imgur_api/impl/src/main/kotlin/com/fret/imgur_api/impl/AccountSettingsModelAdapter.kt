package com.fret.imgur_api.impl

import com.fret.imgur_api.api.models.account.*
import com.squareup.moshi.*

class AccountSettingsModelAdapter {
    @FromJson
    fun fromJson(jsonReader: JsonReader, nonProDelegate: JsonAdapter<NonProAccountSettingsModel>, proDelegate: JsonAdapter<ProAccountSettingsModel>): AccountSettingsModel? {
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
    fun toJson(jsonWriter: JsonWriter, accountModel: AccountSettingsModel, proDelegate: JsonAdapter<ProAccountSettingsModel>, nonProDelegate: JsonAdapter<NonProAccountSettingsModel>) {
        when (accountModel) {
            is ProAccountSettingsModel -> proDelegate.toJson(jsonWriter, accountModel)
            is NonProAccountSettingsModel -> nonProDelegate.toJson(jsonWriter, accountModel)
        }
    }
}