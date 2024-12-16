import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.exposed.sql.Database

fun main() = application {
    Database.connect("jdbc:sqlite:university.db", driver = "org.sqlite.JDBC")
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}


