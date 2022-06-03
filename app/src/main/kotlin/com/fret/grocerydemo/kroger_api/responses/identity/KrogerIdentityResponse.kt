package com.fret.grocerydemo.kroger_api.responses.identity

import com.fret.grocerydemo.kroger_api.models.identity.UserIDModel
import com.fret.grocerydemo.kroger_api.models.meta.MetaModel

data class KrogerIdentityResponse(
    val data : UserIDModel,
    val meta : MetaModel
)