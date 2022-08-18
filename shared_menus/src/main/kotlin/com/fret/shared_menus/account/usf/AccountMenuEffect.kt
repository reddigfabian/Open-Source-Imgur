package com.fret.shared_menus.account.usf

import com.fret.usf.UsfEffect

sealed class AccountMenuEffect: UsfEffect {
    object DoImgurAuthEffect: AccountMenuEffect()
    object GoToMyAccountEffect: AccountMenuEffect()
}
