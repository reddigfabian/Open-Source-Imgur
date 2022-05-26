package com.fret.grocerydemo.common.extensions

import android.net.Uri

fun Uri.replaceQueryParam(key : String, newValue : String) : Uri {
    val queryParameterNames = queryParameterNames
    val newUriBuilder = buildUpon().clearQuery()
    queryParameterNames.forEach {
        newUriBuilder.appendQueryParameter(it,
            when (it) {
                key -> newValue
                else -> getQueryParameter(it)
            }
        )
    }
    return newUriBuilder.build()
}
