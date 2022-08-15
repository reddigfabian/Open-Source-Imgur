package com.fret.imgur_album.impl.usf

sealed class AlbumEvent {
    object ScreenLoadEvent : AlbumEvent()
    object AccountMenuClickedEvent : AlbumEvent()
}