import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.digiquest.core.Digimon
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

@Composable
fun App() {
    MaterialTheme {
        var text by remember { mutableStateOf("Hello, World!") }
        var buttonScreen by remember { mutableStateOf(true) }
        val digimon = Digimon.builder().name("Testmon").build()
        if(buttonScreen) {
            log.info {"Button View"}
            Button(onClick = {
                text = "Hello, Digital World and ${digimon.getName()}!"
                buttonScreen = false;
            }) {
                Text(text)
            }
        } else {
            log.info {"Column View"}
            Column {
                Text("Line 1")
                Text("Line 2")
                Text(text)
            }
        }
    }
}
