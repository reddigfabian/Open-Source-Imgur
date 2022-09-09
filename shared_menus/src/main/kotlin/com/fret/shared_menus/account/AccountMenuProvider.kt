package com.fret.shared_menus.account

import android.app.Application
import android.content.res.ColorStateList
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.core.view.MenuProvider
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.fret.menus.R
import com.fret.shared_menus.account.usf.AccountMenuEvent
import com.fret.shared_menus.account.usf.AccountMenuViewState
import com.fret.utils.view.toPx
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "AccountMenuProvider"

class AccountMenuProvider(
    application: Application,
    private val lifecycleScope: CoroutineScope,
    private val accountMenuDelegate: AccountMenuDelegate
): MenuProvider {

    private val context = application.applicationContext

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throw throwable
        }

    private val coroutineContext = Dispatchers.Main + SupervisorJob() + exceptionHandler

    private val scope = CoroutineScope(coroutineContext)

    private var menu: Menu? = null

    override fun onPrepareMenu(menu: Menu) {
        super.onPrepareMenu(menu)
    }

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
            nonNullMenu.findItem(R.id.menuActionAccount)?.let { menuActionAccount ->
                when (viewState.isAuthorized) {
                    true -> {
                        MenuItemCompat.setIconTintList(menuActionAccount,null)
                        val imageLoader = ImageLoader.Builder(context).build()
                        val size = 24.toPx
                        val request = ImageRequest.Builder(context)
                            .data("https://i.imgur.com/E0fXx4R_d.png?maxwidth=$size&fidelity=grand")
                            .transformations(CircleCropTransformation())
                            .size(size)
                            .target(
                                onStart = { placeholder ->
                                    // Handle the placeholder drawable.
                                },
                                onSuccess = { result ->
                                    // Handle the successful result.
                                    menuActionAccount.icon = result
                                },
                                onError = { error ->
                                    // Handle the error drawable.
                                    menuActionAccount.setIcon(com.fret.shared_resources.R.drawable.ic_account_circle)
                                }
                            )
                            .build()
                        imageLoader.enqueue(request)
                    }
                    false -> {
                        MenuItemCompat.setIconTintList(menuActionAccount, ColorStateList.valueOf(ContextCompat.getColor(context, com.fret.shared_resources.R.color.imgur_green)))
                        menuActionAccount.setIcon(com.fret.shared_resources.R.drawable.ic_account_circle_outline)
                    }
                }
            }
        }
    }
}