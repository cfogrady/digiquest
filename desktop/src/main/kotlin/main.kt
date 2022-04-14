import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.digiquest.common.App
import com.digiquest.core.PlatformProperties
import com.digiquest.core.dcom.DComManager
import com.digiquest.desktop.dcom.DesktopDComPortFactory
import com.digiquest.desktop.util.DesktopFileChooser
import com.digiquest.desktop.util.DesktopFileManager
import com.digiquest.desktop.util.DesktopSpriteLoader

fun main() = application {
    val dComManager = DComManager(DesktopDComPortFactory())
    val desktopFileManager = DesktopFileManager()
    desktopFileManager.createRelativeDirectoryIfMissing("");
    val platformProperties = PlatformProperties.builder().relativeLocation(desktopFileManager.appHomeString).capableOfAddingToLibrary(true).build()
    val spriteLoader = DesktopSpriteLoader(desktopFileManager)
    val desktopFileChooser = DesktopFileChooser()
    val app = App.createApp(dComManager, platformProperties, spriteLoader, desktopFileChooser)
    Window(onCloseRequest = {
        dComManager.close()
        exitApplication()
    }) {
        app.runAppUI()
    }
}
