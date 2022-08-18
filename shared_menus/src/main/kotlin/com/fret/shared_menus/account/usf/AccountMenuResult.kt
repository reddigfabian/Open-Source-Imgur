package com.fret.shared_menus.account.usf

import com.fret.usf.UsfResult

sealed class AccountMenuResult: UsfResult {
    data class CreateMenuResult(val isAuthorized: Boolean) : AccountMenuResult()
    data class AccountMenuClickedResult(val isAuthorized: Boolean): AccountMenuResult()
    data class AuthSucceededResult(val isAuthorized: Boolean): AccountMenuResult()
}
