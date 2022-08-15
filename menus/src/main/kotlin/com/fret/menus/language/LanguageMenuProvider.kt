package com.fret.menus.language

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import com.fret.di.AppScope
import com.fret.menus.R
import com.squareup.anvil.annotations.ContributesBinding
import java.util.*
import javax.inject.Inject

class LanguageMenuProvider(private val languageSelectListener: LanguageSelectListener = LanguageSelectListener()): MenuProvider {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.album_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        val selectedLocale = when (menuItem.itemId) {
            R.id.language_de -> Locale.GERMAN
            R.id.language_en -> Locale.ENGLISH
            else -> return false
        }
        selectedLocale?.let {
            languageSelectListener.languageSelected(
                it
            )
        }
        return true
    }
}