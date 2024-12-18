package views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import logic.University
import logic.User



data class MainMenuView(val user: User) : Screen {
    val solent = University("solent")
    val spark = solent.createBuilding("The Spark", "TS")
    val collins = solent.createBuilding("Herbert Collins", "HS")

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        MaterialTheme {
            Column(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
                Row() {Button(onClick = {navigator.push(SearchForRoomView(user, solent, false))}) { Text("Search For Rooms By OS and Building") }}
                Row() {Button(onClick = {navigator.push(SearchForRoomView(user, solent, true))}) { Text("Book A Computer") }}
                Row() {Button(onClick = {}) { Text("View Booking") }}
                Row() {Button(onClick = {}) { Text("Cancel Booking") }}
                if (user.getUserType() == "Admin") {
                    Row() {Button(onClick = {}) { Text("View Bookings for specific time of day") }}
                    Row() {Button(onClick = {}) { Text("Add a new room")}}
                    Row() {Button(onClick = {}) { Text("Change Room System Type")}}
                }
                Row() {Button(onClick = {}) { Text("View Booked Computers by room")}}
            }
            Column { Button(onClick = {navigator.popUntilRoot()}) {Text("Log Out")} }
        }
    }
}