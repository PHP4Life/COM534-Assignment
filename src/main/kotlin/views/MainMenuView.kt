////////////////////////// MainMenuView.kt //////////////////////////////////////////
///////////////////////// Author: Edward Kirr ///////////////////////////////////////
//// Description: The landing page for when the user is successfully registered /////
// This acts as the entry point to all the functionality of the program ////////////

package views

import androidx.compose.foundation.layout.*
import logic.University
import logic.User
import views.adminViews.AddNewRoomView
import views.adminViews.ChangeRoomOSTypeView
import views.adminViews.SearchDailyBookingsView

import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

data class MainMenuView(val user: User) : Screen {
    // Parameters: User - Takes in a user object
    // This class is responsible for creating the university object and building objects
    // that are used throughout the entire application
    @Composable
    override fun Content() {
        val solent = University("solent")
        val spark = solent.createBuilding("The Spark", "TS")
        val collins = solent.createBuilding("Herbert Collins", "HC")
        val navigator = LocalNavigator.currentOrThrow

        MaterialTheme {
            Column(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
                Row {
                    Text("Welcome ${user.name}! ", style = MaterialTheme.typography.h6)}
                Row {Text("Select a menu from below: ")}
                Row {Button(modifier = Modifier.width(400.dp), onClick = {navigator.push(SearchForRoomView(user, solent, false))}) { Text("Search For Rooms By OS and Building") }}
                Row {Button(modifier = Modifier.width(400.dp), onClick = {navigator.push(SearchForRoomView(user, solent, true))}) { Text("Book A Computer") }}
                Row {Button(modifier = Modifier.width(400.dp), onClick = {navigator.push(ViewAndCancelBookingsView(user))}) { Text("View & Cancel Bookings") }}
                if (user.getUserType() == "Admin") {
                    Row {Button(modifier = Modifier.width(400.dp), onClick = {navigator.push(SearchDailyBookingsView(solent))}) { Text("View Room Bookings for specific time of day") }}
                    Row {Button(modifier = Modifier.width(400.dp), onClick = {navigator.push(AddNewRoomView(solent))}) { Text("Add a new room")}}
                    Row {Button(modifier = Modifier.width(400.dp), onClick = {navigator.push(ChangeRoomOSTypeView(solent))}) { Text("Change Room System Type")}}
                }
                Row {Button(modifier = Modifier.width(400.dp), onClick = {navigator.push(ComputerBookingsSearchView(user, solent))}) { Text("Graphically View Booked Computers by room")}}
            }
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.BottomCenter)) { Button(onClick = {navigator.popUntilRoot()}) {Text("Log Out")} }
        }
    }
}