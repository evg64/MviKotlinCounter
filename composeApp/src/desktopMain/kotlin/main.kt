import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import ru.chumak.mvikotlincounter.instancekeeper.DesktopInstanceKeeper
import ru.chumak.mvikotlincounter.view.CounterController
import ru.chumak.mvikotlincounter.view.CounterControllerImpl

fun main() {
    val lifecycle = LifecycleRegistry()
    val controller = CounterControllerImpl(
        storeFactory = DefaultStoreFactory(),
        lifecycle = lifecycle,
        instanceKeeper = DesktopInstanceKeeper(),
    )
    application {
        val windowState = rememberWindowState()
        LifecycleController(lifecycle, windowState)
        Window(
            title = "MVIKotlinCounter",
            state = windowState,
            onCloseRequest = ::exitApplication,
        ) {
            val model = controller.state.collectAsState(CounterController.Model())
            App(
                model = model.value,
                onIncrementClick = {
                    controller.consume(CounterController.Event.Increment)
                },
                onDecrementClick = {
                    controller.consume(CounterController.Event.Decrement)
                },
            )
        }
    }
}

@Composable
fun LifecycleController(lifecycleRegistry: LifecycleRegistry, windowState: WindowState) {
    LaunchedEffect(lifecycleRegistry, windowState) {
        snapshotFlow(windowState::isMinimized).collect { isMinimized ->
            if (isMinimized) {
                lifecycleRegistry.stop()
            } else {
                lifecycleRegistry.resume()
            }
        }
    }

    DisposableEffect(lifecycleRegistry) {
        lifecycleRegistry.create()
        onDispose(lifecycleRegistry::destroy)
    }
}
