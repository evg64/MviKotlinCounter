package ru.chumak.mvikotlincounter.view

import kotlinx.coroutines.flow.Flow

/**
 *
 *
 * @author Evgeny Chumak
 **/
interface CounterController {

    val state: Flow<Model>

    fun consume(event: Event)

    data class Model(val count: Long = 0L)

    sealed class Event {
        object Increment : Event()
        object Decrement : Event()
    }
}