package ru.chumak.mvikotlincounter.view

import com.arkivanov.mvikotlin.core.store.Store

/**
 *
 *
 * @author Evgeny Chumak
 **/
interface CounterStore : Store<CounterStore.Intent, CounterStore.State, Nothing> {

    sealed class Intent {
        object Increment : Intent()
        object Decrement : Intent()
    }

    data class State(
        val count: Long = 0L,
    )
}