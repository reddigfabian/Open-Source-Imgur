# Open Source Imgur

## Setup

This project was built using Android Studio Electric Eel | 2022.1.1 Canary 2 so we can't guarantee there won't be some incompatibilities
with older Android Studio versions.

## About

This project is meant to act as a proof of concept for comparing varying solutions for different challenges Android Developers face.
In some cases these implementations may be iterative (e.g. going from no DI -> Dagger -> Anvil -> Tangle) while others may be more
direct comparisons (e.g. RxJava vs. Coroutines+Flows or XML vs. Compose).

For now, we'll use this README to track progress and put different implementations in different branches. Eventually we might build ways
to swap out implementations on the fly either at build time or, if possible, even at run time.

Currently this project covers the following categories in their corresponding branches:

* Dependency Injection 
    * No dependency injection at all - `no_dependency_injection`
      * This mostly exists to highlight the need for some form of dependency injection as well as some of the challenges a dependency 
        injection framework can help address such as scoped singletons.
    * Vanilla Dagger - `vanilla_dagger`
      * Uses only Dagger tools to do injection. This sets up any desired dependency injections and allows us to ensure the injection path
        "works" in a vanilla implementation before transitioning it to Anvil and multiple modules.
    * Anvil - `anvil`
      * Work in progress. (Not sure what to do here in order to inject ViewModels while still gaining all the speed benefits of anvil.
        Trying Tangle for this instead.)
    * Tangle - `tangle`
      * Instead of using Anvil directly, we introduce it via Tangle. This gives us access to injectable Android components such as
        Activities, Fragments, and, most importantly, ViewModels.
    * Tangle with multiple modules - `tangle_multi_module`
      * Building on top of the Tangle integration, we split everything up into modules with 'api' and 'impl' submodules and add in a "detail"
        view in order to explore navigation across multiple modules via implicit deeplinks.
  
        
