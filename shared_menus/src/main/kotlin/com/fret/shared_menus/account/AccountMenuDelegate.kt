package com.fret.shared_menus.account

import androidx.lifecycle.LifecycleObserver
import com.fret.shared_menus.account.usf.AccountMenuEffect
import com.fret.shared_menus.account.usf.AccountMenuEvent
import com.fret.shared_menus.account.usf.AccountMenuResult
import com.fret.shared_menus.account.usf.AccountMenuViewState
import com.fret.usf.UsfEvent
import kotlinx.coroutines.flow.Flow

interface AccountMenuDelegate {
    val accountMenuEvents: Flow<AccountMenuEvent>
    val accountMenuResults: Flow<AccountMenuResult>
    val accountMenuViewState: Flow<AccountMenuViewState>
    val accountMenuEffects: Flow<AccountMenuEffect>
    suspend fun processEvent(event: AccountMenuEvent)
}