package com.fret.shared_menus.account

import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.lifecycle.*
import com.fret.menus.R
import com.fret.shared_menus.account.usf.AccountMenuEvent
import com.fret.shared_menus.account.usf.AccountMenuViewState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "AccountMenuProvider"

class AccountMenuProvider(private val accountMenuDelegate: AccountMenuDelegate): MenuProvider {

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throw throwable
        }

    private val context = Dispatchers.Main + SupervisorJob() + exceptionHandler

    private val scope = CoroutineScope(context)

    private var menu: Menu? = null

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        Log.d(TAG, "onCreateMenu")
        menuInflater.inflate(R.menu.list_menu, menu)
        this.menu = menu
        scope.launch {
            accountMenuDelegate.accountMenuViewState.collectLatest { viewState ->
                Log.d(TAG, "got a new viewState from flow: $viewState")
                render(viewState)
            }
        }
        scope.launch {
            accountMenuDelegate.processEvent(AccountMenuEvent.CreateMenuEvent)
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        Log.d(TAG, "onMenuItemSelected: $menuItem")
        scope.launch {
            accountMenuDelegate.processEvent(AccountMenuEvent.AccountMenuItemSelectedEvent(menuItem.itemId))
        }
        return true
    }

    private fun render(viewState: AccountMenuViewState) {
        Log.d(TAG, "render: $viewState")
        menu?.let { nonNullMenu ->
            when (viewState.isAuthorized) {
                true -> nonNullMenu.findItem(R.id.menuActionAccount)?.setIcon(com.fret.shared_resources.R.drawable.ic_account_circle)
                false -> nonNullMenu.findItem(R.id.menuActionAccount)?.setIcon(com.fret.shared_resources.R.drawable.ic_account_circle_outline)
            }
        }
    }
}