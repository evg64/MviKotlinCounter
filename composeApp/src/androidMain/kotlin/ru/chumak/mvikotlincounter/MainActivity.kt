package ru.chumak.mvikotlincounter

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.essenty.instancekeeper.instanceKeeper
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import ru.chumak.mvikotlincounter.view.CounterController
import ru.chumak.mvikotlincounter.view.CounterControllerImpl

class MainActivity : ComponentActivity() {

    private lateinit var controller: CounterControllerImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controller = CounterControllerImpl(
            storeFactory = DefaultStoreFactory(),
            lifecycle = essentyLifecycle(),
            instanceKeeper = instanceKeeper(),
        )

        setContent {
            val model = controller.state.collectAsStateWithLifecycle(CounterController.Model())
            App(
                model = model.value,
                onIncrementClick = { controller.consume(CounterController.Event.Increment) },
                onDecrementClick = { controller.consume(CounterController.Event.Decrement) } ,
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(CounterController.Model(count = 2), onIncrementClick = {}, onDecrementClick = {})
}