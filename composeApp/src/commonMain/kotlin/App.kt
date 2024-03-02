import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import mvikotlincounter.composeapp.generated.resources.Res
import mvikotlincounter.composeapp.generated.resources.compose_multiplatform
import ru.chumak.mvikotlincounter.view.CounterController

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App(
    model: CounterController.Model,
    onIncrementClick: () -> Unit,
    onDecrementClick: () -> Unit,
) {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            TextField(value = TextFieldValue(model.count.toString()), onValueChange = {}, enabled = false)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(modifier = Modifier.padding(16.dp), onClick = onDecrementClick) {
                    Text("   -1   ")
                }
                Button(modifier = Modifier.padding(16.dp), onClick = onIncrementClick) {
                    Text("   +1   ")
                }
            }
        }
    }
}