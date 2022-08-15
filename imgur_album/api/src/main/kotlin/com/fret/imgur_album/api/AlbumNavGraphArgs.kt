package com.fret.imgur_album.api

import android.os.Bundle

data class AlbumNavGraphArgs(val albumHash: String) {
    companion object {
        private const val ARG_DETAIL_ITEM_ID = "albumHash"

        fun fromArgs(args: Bundle?): AlbumNavGraphArgs {
            args?.getString(ARG_DETAIL_ITEM_ID)?.let {
                return AlbumNavGraphArgs(it)
            } ?: run {
                throw IllegalArgumentException("bundle is null or does not contain key $ARG_DETAIL_ITEM_ID")
            }
        }
    }

    fun toBundle() : Bundle {
        val b = Bundle()
        b.putString(ARG_DETAIL_ITEM_ID, albumHash)
        return b
    }
}