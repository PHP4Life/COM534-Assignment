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
import views.adminViews.AddNewRoomView
import views.adminViews.ChangeRoomOSTypeView
import views.adminViews.SearchDailyBookingsView


data class MainMenuView(val user: User) : Screen {

    @Composable
    override fun Content() {
        val solent = University("solent")
        val spark = solent.createBuilding("The Spark", "TS")
        val collins = solent.createBuilding("Herbert Collins", "HC")
        val navigator = LocalNavigator.currentOrThrow

        MaterialTheme {
            Column(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
                Row() {Button(onClick = {navigator.push(SearchForRoomView(user, solent, false))}) { Text("Search For Rooms By OS and Building") }}
                Row() {Button(onClick = {navigator.push(SearchForRoomView(user, solent, true))}) { Text("Book A Computer") }}
                Row() {Button(onClick = {navigator.push(ViewAndCancelBookingsView(user))}) { Text("View & Cancel Bookings") }}
                if (user.getUserType() == "Admin") {
                    Row() {Button(onClick = {navigator.push(SearchDailyBookingsView(solent))}) { Text("View Room Bookings for specific time of day") }}
                    Row() {Button(onClick = {navigator.push(AddNewRoomView(solent))}) { Text("Add a new room")}}
                    Row() {Button(onClick = {navigator.push(ChangeRoomOSTypeView(solent))}) { Text("Change Room System Type")}}
                }
                Row() {Button(onClick = {navigator.push(ComputerBookingsSearchView(user, solent))}) { Text("Graphically View Booked Computers by room")}}
            }
            Column { Button(onClick = {navigator.popUntilRoot()}) {Text("Log Out")} }
        }
    }
}