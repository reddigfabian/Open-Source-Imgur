package com.fret.menus.language

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.*

class LanguageSelectListenerImpl: LanguageSelectListener {
    override fun languageSelected(locale: Locale) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale))
    }
}