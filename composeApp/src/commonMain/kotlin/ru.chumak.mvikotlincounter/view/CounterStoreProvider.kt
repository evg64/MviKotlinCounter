package ru.chumak.mvikotlincounter.view

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.store.create
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import kotlin.coroutines.CoroutineContext

/**
 *
 *
 * @author Evgeny Chumak
 **/
class CounterStoreProvider(
    private val storeFactory: StoreFactory,
    private val mainContext: CoroutineContext,
) {

    fun provideCounterStore(): CounterStore {
        return object : CounterStore, Store<CounterStore.Intent, CounterStore.State, Nothing> by storeFactory.create(
            name = "CounterStore",
            initialState = CounterStore.State(),
            bootstrapper = SimpleBootstrapper(Action.Init(0)),
            executorFactory = { ExecutorImpl() },
            reducer = ReducerImpl(),
        ) {}
    }

    private sealed class Action {
        class Init(val initialCount: Long) : Action()
        object Increment : Action()
        object Decrement : Action()
    }

    private sealed class Msg {
        class UpdateCounter(val count: Long) : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<CounterStore.Intent, Action, CounterStore.State, Msg, Nothing>(mainContext) {

        override fun executeAction(action: Action, getState: () -> CounterStore.State) {
            when (action) {
                is Action.Init -> Msg.UpdateCounter(count = action.initialCount)
                Action.Increment -> Msg.UpdateCounter(count = getState.invoke().count + 1)
                Action.Decrement -> Msg.UpdateCounter(count = getState.invoke().count - 1)
            }
        }

        override fun executeIntent(intent: CounterStore.Intent, getState: () -> CounterStore.State) {
            val newCount = when (intent) {
                CounterStore.Intent.Increment -> getState.invoke().count + 1
                CounterStore.Intent.Decrement -> getState.invoke().count - 1
            }
            dispatch(Msg.UpdateCounter(newCount))
        }
    }

    private class ReducerImpl : Reducer<CounterStore.State, Msg> {
        override fun CounterStore.State.reduce(msg: Msg): CounterStore.State {
            return when(msg) {
                is Msg.UpdateCounter -> copy(count = msg.count)
            }
        }
    }
}