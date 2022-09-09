package com.fret.shared_menus.account

import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResult
import com.fret.menus.R
import com.fret.shared_menus.account.usf.AccountMenuEffect
import com.fret.shared_menus.account.usf.AccountMenuEvent
import com.fret.shared_menus.account.usf.AccountMenuResult
import com.fret.shared_menus.account.usf.AccountMenuViewState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import javax.inject.Inject

private const val TAG = "DefaultAccountMenuDeleg"

class DefaultAccountMenuDelegate @Inject constructor(
    private val imgurAuthState: AuthState
): AccountMenuDelegate {
    override val accountMenuEvents = MutableSharedFlow<AccountMenuEvent>()
    override val accountMenuResults = accountMenuEvents.mapNotNull {event ->
        Log.d(TAG, "eventToResult: $event")
        when (event) {
            AccountMenuEvent.CreateMenuEvent -> {
                Log.d(TAG, "eventToResult: emitting CreateMenuResult with isAuthorized = ${imgurAuthState.isAuthorized}")
                onCreateMenu()
            }
            is AccountMenuEvent.AccountMenuItemSelectedEvent -> {
                when (event.menuItemId) {
                    R.id.menuActionAccount -> {
                        Log.d(TAG, "eventToResult: emitting AccountMenuClickedResult with isAuthorized = ${imgurAuthState.isAuthorized}")
                        onAccountMenuClicked()
                    }
                    else -> {
                        null
                    }
                }
            }
            AccountMenuEvent.AuthSucceededEvent -> {
                Log.d(TAG, "eventToResult: emitting AuthSucceededResult with isAuthorized = ${imgurAuthState.isAuthorized}")
                onAuthSucceeded()
            }
        }
    }
    override val accountMenuViewState = accountMenuResults.scan(
        AccountMenuViewState(imgurAuthState.isAuthorized)
    ) { previousState, result ->
        Log.d(TAG, "resultToState: $previousState | $result")
        when (result) {
            is AccountMenuResult.CreateMenuResult -> {
                Log.d(TAG, "resultToState: emitting copy state with isAuthorized = ${result.isAuthorized}")
                previousState.copy(
                    isAuthorized = result.isAuthorized
                )
            }
            is AccountMenuResult.AccountMenuClickedResult -> {
                Log.d(TAG, "resultToState: emitting copy state with isAuthorized = ${result.isAuthorized}")
                previousState.copy(
                    isAuthorized = result.isAuthorized
                )
            }
            is AccountMenuResult.AuthSucceededResult -> {
                Log.d(TAG, "resultToState: emitting copy state with isAuthorized = ${result.isAuthorized}")
                previousState.copy(
                    isAuthorized = result.isAuthorized
                )
            }
            else -> {
                Log.d(TAG, "resultToState: emitting previousState $previousState")
                previousState
            }
        }
    }
    override val accountMenuEffects = accountMenuResults.mapNotNull { result ->
        Log.d(TAG, "resultToEffect: $result")
        when (result) {
            is AccountMenuResult.AccountMenuClickedResult -> {
                if (result.isAuthorized) {
                    Log.d(TAG, "resultToEffect: emitting GoToMyAccountEffect")
                    AccountMenuEffect.GoToMyAccountEffect
                } else {
                    Log.d(TAG, "resultToEffect: emitting DoImgurAuthEffect")
                    AccountMenuEffect.DoImgurAuthEffect
                }
            }
            is AccountMenuResult.AuthSucceededResult -> {
                if (result.isAuthorized) {
                    Log.d(TAG, "resultToEffect: emitting GoToMyAccountEffect")
                    AccountMenuEffect.GoToMyAccountEffect
                } else {
                    Log.d(TAG, "resultToEffect: null")
                    null
                }
            }
            else -> null
        }
    }

    private fun onCreateMenu(): AccountMenuResult = AccountMenuResult.CreateMenuResult(imgurAuthState.isAuthorized)
    private fun onAccountMenuClicked(): AccountMenuResult = AccountMenuResult.AccountMenuClickedResult(imgurAuthState.isAuthorized)
    private fun onAuthSucceeded(): AccountMenuResult = AccountMenuResult.AuthSucceededResult(imgurAuthState.isAuthorized)

    override suspend fun processEvent(event: AccountMenuEvent) {
        accountMenuEvents.emit(event)
    }

    override val onAuthActivityResult: suspend (result: ActivityResult) -> Unit = { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                result.data?.let { intent ->
                    val resp = AuthorizationResponse.fromIntent(intent)
                    val ex = AuthorizationException.fromIntent(intent)
                    Log.d(TAG, "imgurAuthState: $imgurAuthState")
                    imgurAuthState.update(resp, ex)
                    if (ex != null) {
                        Log.e(TAG, "AppAuth Activity result returned exception", ex)
                    } else {
                        Log.d(TAG, "AppAuth Activity result returned successfully")
                        processEvent(AccountMenuEvent.AuthSucceededEvent)
                    }

                } ?: run {
                    Log.e(TAG, "AppAuth Activity result returned with a null data Intent")
                }
            }
            Activity.RESULT_CANCELED -> {
                Log.d(TAG, "AppAuth Activity result returned due to user cancellation")
            }
            else -> {
                Log.e(TAG, "AppAuth Activity result returned with unexpected Activity result")
            }
        }
    }
}