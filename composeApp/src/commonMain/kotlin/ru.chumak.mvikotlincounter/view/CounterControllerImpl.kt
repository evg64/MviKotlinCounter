package ru.chumak.mvikotlincounter.view

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.view.ViewRenderer
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import com.arkivanov.mvikotlin.extensions.coroutines.states

/**
 *
 *
 * @author Evgeny Chumak
 **/
class CounterControllerImpl(
    private val storeFactory: StoreFactory,
    private val lifecycle: Lifecycle,
    instanceKeeper: InstanceKeeper,
) : CounterController {
    private val _state = MutableStateFlow(CounterController.Model())
    override val state: Flow<CounterController.Model>
        get() = _state

    private val eventsChannel = Channel<CounterController.Event>(capacity = Int.MAX_VALUE)

    private val events = eventsChannel.receiveAsFlow()
    private val store = instanceKeeper.getStore {
        CounterStoreProvider(
            storeFactory = storeFactory,
            mainContext = Dispatchers.Main,
        ).provideCounterStore()
    }

    init {
        initBindings()
        lifecycle.doOnDestroy { store.dispose() }
    }

    private fun initBindings() {
        bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY, Dispatchers.Unconfined) {
            events.map(::eventToIntent) bindTo store
        }

        bind(lifecycle, BinderLifecycleMode.START_STOP, Dispatchers.Unconfined) {
            val map: Flow<CounterController.Model> =
                store.states.map { statesToModel(it) }
            map bindTo MyViewRenderer()
        }
    }


    override fun consume(event: CounterController.Event) {
        eventsChannel.trySend(event)
    }

    private inner class MyViewRenderer : ViewRenderer<CounterController.Model> {
        override fun render(model: CounterController.Model) {
            _state.value = model
        }
    }
}