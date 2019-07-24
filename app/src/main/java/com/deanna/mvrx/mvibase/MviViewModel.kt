package com.deanna.mvrx.mvibase

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.BuildConfig
import com.airbnb.mvrx.MvRxState
import io.reactivex.Observable

abstract class MviViewModel<I : MviIntent, S : MvRxState>(initialState: S) : BaseMvRxViewModel<S>(initialState, debugMode = BuildConfig.DEBUG) {
    abstract fun processIntents(intents: Observable<I>)
}
