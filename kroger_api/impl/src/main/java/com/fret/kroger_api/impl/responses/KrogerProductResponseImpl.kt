package com.fret.kroger_api.impl.responses

import com.fret.kroger_api.api.responses.KrogerProductResponse

data class KrogerProductResponseImpl(override val products: List<String>) : KrogerProductResponse