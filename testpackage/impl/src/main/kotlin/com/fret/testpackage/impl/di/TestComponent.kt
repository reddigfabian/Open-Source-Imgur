package com.fret.testpackage.impl.di

import com.fret.di.AppScope
import com.fret.di.SingleIn
import com.squareup.anvil.annotations.ContributesSubcomponent
import com.squareup.anvil.annotations.ContributesTo

@SingleIn(TestScope::class)
@ContributesSubcomponent(scope = TestScope::class, parentScope = AppScope::class)
interface TestComponent {
    @ContributesSubcomponent.Factory
    interface Factory {
        fun create(): TestComponent
    }

    @ContributesTo(AppScope::class)
    interface ParentBindings {
        fun testComponentBuilder(): Factory
    }
}