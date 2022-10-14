package com.fret.testpackage.impl.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fret.testpackage.impl.R
import com.fret.testpackage.impl.usf.TestViewState
import com.fret.testpackage.impl.usf.TestEvent
import com.fret.testpackage.impl.usf.TestResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch

class TestViewModel: ViewModel() {

    private var inputText: String? = null

    private val events = MutableSharedFlow<TestEvent>()
    private val results = events.map {
        when (it) {
            TestEvent.ScreenLoadEvent -> onScreenLoad()
            is TestEvent.TextChangeEvent -> onTextChange(it.newText)
            TestEvent.SubmitPressEvent -> onSubmit()
        }
    }

    val viewState = results.scan(TestViewState()) { prevState, result ->
        when (result) {
            TestResult.ScreenLoadResult -> {
                prevState.copy(buttonText = getButtonTextResForInputText(inputText))
            }
            is TestResult.TextChangeResult -> {
                prevState.copy(buttonText = getButtonTextResForInputText(result.newText))
            }
            is TestResult.SubmitResult -> {
                prevState.copy(showText = result.submitText)
            }
        }
    }.distinctUntilChanged()

    val viewEffects = results.mapNotNull { result ->
        null
    }

    fun processEvent(event: TestEvent) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    private fun onScreenLoad(): TestResult.ScreenLoadResult {
        return TestResult.ScreenLoadResult
    }

    private fun onTextChange(newText: String?): TestResult.TextChangeResult {
        inputText = newText
        return TestResult.TextChangeResult(newText)
    }

    private fun onSubmit(): TestResult.SubmitResult {
        return TestResult.SubmitResult(inputText)
    }

    private fun getButtonTextResForInputText(input: String?): Int {
        return if (input.isNullOrBlank()) {
            R.string.clear
        } else {
            R.string.submit
        }
    }
}