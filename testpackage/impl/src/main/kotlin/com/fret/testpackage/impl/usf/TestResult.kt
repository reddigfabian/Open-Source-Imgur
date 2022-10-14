package com.fret.testpackage.impl.usf

sealed class TestResult {
    object ScreenLoadResult: TestResult()
    data class TextChangeResult(val newText: String?): TestResult()
    data class SubmitResult(val submitText: String?): TestResult()
}