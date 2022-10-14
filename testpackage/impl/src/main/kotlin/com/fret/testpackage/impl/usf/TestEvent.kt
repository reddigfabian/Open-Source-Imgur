package com.fret.testpackage.impl.usf

sealed class TestEvent {
    object ScreenLoadEvent : TestEvent()
    data class TextChangeEvent(val newText: String?) : TestEvent()
    object SubmitPressEvent : TestEvent()
}