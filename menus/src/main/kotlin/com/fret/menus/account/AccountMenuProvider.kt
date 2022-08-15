package com.fret.menus.account

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import com.fret.menus.R
import java.util.*

class AccountMenuProvider(private val accountMenuClickListener: AccountMenuClickListener): MenuProvider {

    constructor(clickFunction: () -> Unit): this(object: AccountMenuClickListener {
        override fun accountMenuClicked() {
            clickFunction.invoke()
        }
    })

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.list_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menuActionAccount-> {
                accountMenuClickListener.accountMenuClicked()
                true
            }
            else -> false
        }
    }
}