package com.fret.utils

import android.os.Bundle
import androidx.lifecycle.ViewModel

interface ViewModelFactory<VM : ViewModel, S: Any?> {
    fun create(args: S): VM
}