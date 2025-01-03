////////////////////////////// Main.kt //////////////////////////////////////////
///////////////////////// Author: Edward Kirr //////////////////////////////////
////// Description: Responsible for the main entry point of the program ///////

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.exposed.sql.Database

fun main() = application {
    Database.connect("jdbc:sqlite:university.db", driver = "org.sqlite.JDBC")
    Window(title = "University Booking System", onCloseRequest = ::exitApplication) {
        App()
    }
}


