package views

import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import logic.User



data class MainMenuView(val user: User) : Screen {
    @Composable
    override fun Content() {
        MaterialTheme {
            when (user.getUserType()) {
                "Admin" -> {
                    // Admin menu items
                    Button(onClick = {}) { Text("View Bookings for specific time") }
                    Button(onClick = {}) { Text("Add a new room") }
                }

                "Regular" -> {
                    // User menu items
                    Button(onClick = {}) { Text("Search For Rooms By OS") }
                    Button(onClick = {}) { Text("Book Computer") }
                    Button(onClick = {}) { Text("View Bookings") }
                    Button(onClick = {}) { Text("Cancel Bookings") }
                }
            }
        }
    }
}