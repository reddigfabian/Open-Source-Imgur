package com.fret.testpackage.impl.di

import com.fret.testpackage.impl.views.TestFragment
import com.squareup.anvil.annotations.ContributesTo

@ContributesTo(TestScope::class)
interface TestBindings {
    fun inject(fragment: TestFragment)
}