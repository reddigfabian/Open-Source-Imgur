package com.fret.shared_menus.account.usf

import com.fret.usf.UsfEvent

sealed class AccountMenuEvent: UsfEvent {
    object CreateMenuEvent: AccountMenuEvent()
    data class AccountMenuItemSelectedEvent(val menuItemId: Int): AccountMenuEvent()
    object AuthSucceededEvent: AccountMenuEvent()
}
