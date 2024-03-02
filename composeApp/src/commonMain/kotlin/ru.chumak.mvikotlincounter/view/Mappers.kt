package ru.chumak.mvikotlincounter.view

/**
 *
 *
 * @author Evgeny Chumak
 **/

fun eventToIntent(event: CounterController.Event): CounterStore.Intent {
    return when (event) {
        CounterController.Event.Increment -> CounterStore.Intent.Increment
        CounterController.Event.Decrement -> CounterStore.Intent.Decrement
    }
}

val statesToModel: (CounterStore.State) -> CounterController.Model =
    { state -> CounterController.Model(count = state.count) }