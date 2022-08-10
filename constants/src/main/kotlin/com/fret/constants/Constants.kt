package com.fret.constants

object Constants {

    const val IMGUR_AUTH_API_BASE_URL = "https://api.imgur.com/"
    const val IMGUR_API_BASE_URL = "https://api.imgur.com/3/"
    const val IMGUR_OAUTH2_AUTH_CODE = "${IMGUR_AUTH_API_BASE_URL}oauth2/authorize"
    const val IMGUR_OAUTH2_ACCESS_TOKEN = "${IMGUR_AUTH_API_BASE_URL}oauth2/token"

    //open-source-imgur://com.fret.open_source_imgur/list_fragment
    const val LOCALHOST_AUTH_REDIRECT = "https://192.168.1.17:8080/"
    const val AUTH_REDIRECT = "https://auth.reddigfabian.com"
    const val DEEPLINK_SCHEME = "open-source-imgur"
    const val DEEPLINK_HOST = "com.fret.open_source_imgur"
    const val DEEPLINK_URI = "$DEEPLINK_SCHEME://$DEEPLINK_HOST"
    const val DEEPLINK_LIST_FRAGMENT = "/list_fragment"
    const val DEEPLINK_REDIRECT_LIST = "$DEEPLINK_URI$DEEPLINK_LIST_FRAGMENT"
    const val DEEPLINK_ACCESS_TOKEN_PARAM = "access_token"
    const val DEEPLINK_EXPIRES_IN_PARAM = "expires_in"
    const val DEEPLINK_REFRESH_TOKEN_PARAM = "refresh_token"
    const val DEEPLINK_REDIRECT_WITH_ACCESS_TOKEN_PARAM = "$DEEPLINK_REDIRECT_LIST?$DEEPLINK_ACCESS_TOKEN_PARAM={$DEEPLINK_ACCESS_TOKEN_PARAM}"
}
