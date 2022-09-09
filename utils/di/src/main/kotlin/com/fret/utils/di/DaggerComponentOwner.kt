package com.fret.utils.di

interface DaggerComponentOwner {
    /** This is either a component, or a list of components. */
    val daggerComponent: Any
}