package com.fret.utils.view

import android.content.res.Resources

val Int.toDp: Int
   get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.toPx: Int
   get() = (this * Resources.getSystem().displayMetrics.density).toInt()