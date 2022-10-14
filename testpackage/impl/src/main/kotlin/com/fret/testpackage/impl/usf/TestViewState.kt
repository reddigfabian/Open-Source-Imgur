package com.fret.testpackage.impl.usf

import androidx.annotation.StringRes
import com.fret.testpackage.impl.R

data class TestViewState(
    val showText: String? = null,
    @StringRes val buttonText: Int = R.string.clear
)