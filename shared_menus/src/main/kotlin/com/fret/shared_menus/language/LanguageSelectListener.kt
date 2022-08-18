package com.fret.shared_menus.language

import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.fret.di.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.anvil.annotations.ContributesTo
import java.util.*
import javax.inject.Inject

open class LanguageSelectListener {
    @CallSuper
    open fun languageSelected(locale: Locale) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale))
    }
}