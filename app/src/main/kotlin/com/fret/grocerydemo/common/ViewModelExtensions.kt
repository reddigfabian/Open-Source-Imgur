package com.fret.grocerydemo.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@PublishedApi
internal const val DEFAULT_KEY = "com.fret.grocerydemo.common.ViewModelProvider"

inline fun <reified VM : ViewModel> ViewModelStoreOwner.getViewModel(
    factory: ViewModelProvider.Factory,
    key: String? = null
): VM = getViewModel(VM::class.java, factory, key)

inline fun <VM : ViewModel> ViewModelStoreOwner.getViewModel(
    clazz: Class<VM>,
    factory: ViewModelProvider.Factory,
    key: String? = null
): VM {
    val generatedKey = key ?: "$DEFAULT_KEY:${clazz.canonicalName}"
    return ViewModelProvider(this, factory).get(generatedKey, clazz)
}

@Suppress("unused")
inline fun <reified VM : ViewModel> ViewModelStoreOwner.getViewModel(
    key: String? = null,
    noinline factory: () -> ViewModelProvider.Factory = { ViewModelProvider.NewInstanceFactory() }
): ReadOnlyProperty<ViewModelStoreOwner, VM> {
    return ViewModelDelegate(VM::class.java, key, factory)
}

@Suppress("unused")
inline fun <reified VM : ViewModel> Fragment.getSharedViewModel(
    key: String? = null,
    noinline factory: () -> ViewModelProvider.Factory = { ViewModelProvider.NewInstanceFactory() }
): ReadOnlyProperty<ViewModelStoreOwner, VM> {
    return ViewModelDelegate(VM::class.java, key, factory, true)
}

@Suppress("unused")
inline fun <reified VM : ViewModel, O> O.assistedViewModel(
    key: String? = null,
    defaultArgs: Bundle? = null,
    noinline factory: (state: SavedStateHandle) -> VM
): ReadOnlyProperty<O, VM> where O : ViewModelStoreOwner, O : SavedStateRegistryOwner {
    return AssistedViewModelDelegate(VM::class.java, key, defaultArgs, factory)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified VM : ViewModel, T> Class<T>.isOfViewModel(creator: () -> VM) = when {
    isAssignableFrom(VM::class.java) -> creator() as T
    else -> throw IllegalArgumentException("Unexpected class: ${VM::class.java.simpleName}")
}

@PublishedApi
internal class ViewModelDelegate<in T : ViewModelStoreOwner, out VM : ViewModel>(
    private val clazz: Class<VM>,
    private val key: String? = null,
    private val factory: () -> ViewModelProvider.Factory,
    private val shared: Boolean = false,
) : ReadOnlyProperty<T, VM> {
    private var value: VM? = null
    override fun getValue(thisRef: T, property: KProperty<*>): VM {
        val owner = if (shared && thisRef is Fragment) thisRef.requireActivity() else thisRef
        return value ?: owner.getViewModel(clazz, factory(), key).also { value = it }
    }
}

@PublishedApi
@Suppress("UNCHECKED_CAST")
internal class AssistedViewModelDelegate<in T, out VM : ViewModel>(
    private val clazz: Class<VM>,
    private val key: String? = null,
    private val defaultArgs: Bundle? = null,
    private val factory: (state: SavedStateHandle) -> VM
) : ReadOnlyProperty<T, VM> where T : ViewModelStoreOwner, T : SavedStateRegistryOwner {
    private var value: VM? = null
    private lateinit var stateFactory: AbstractSavedStateViewModelFactory
    override fun getValue(thisRef: T, property: KProperty<*>): VM {
        if (!::stateFactory.isInitialized) {
            stateFactory = object : AbstractSavedStateViewModelFactory(thisRef, defaultArgs) {
                override fun <VM : ViewModel> create(
                    key: String,
                    modelClass: Class<VM>,
                    handle: SavedStateHandle
                ): VM = factory(handle) as VM
            }
        }
        return value ?: thisRef.getViewModel(clazz, stateFactory, key).also { value = it }
    }
}