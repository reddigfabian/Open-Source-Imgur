package com.fret.shared_menus.account

import android.view.MenuItem

interface AccountMenuItemSelectedListener {
    fun onItemSelected(menuItem: MenuItem): Boolean
}