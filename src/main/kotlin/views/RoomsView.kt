////////////////////////// RoomsView.kt //////////////////////////////////////////
///////////////////////// Author: Edward Kirr ///////////////////////////////////
//// Description: Displays all the rooms inside the building or the ones ///////
// for the specified OS based on the filters selected on the previous screen //


package views

import logic.Room
import logic.User

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow



data class RoomView(val user: User, val rooms: List<Room>, val bookComputer: Boolean) : Screen {
    // Parameters:
    // User - this is the logged-in user
    // List<Room> - These are the rooms filtered returned by the previous view
    // Boolean - True, if the user is in the book menu, false if the user picks search
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val selectedRoom = remember { mutableStateOf<Room?>(null) }

        MaterialTheme {
            Column {
                if (rooms.isNotEmpty()) {
                    LazyColumn {
                        items(rooms) { room ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable {},
                                onClick = {
                                    selectedRoom.value = room
                                    if (bookComputer && selectedRoom.value != null) {navigator.push(BookComputerView(user, room))}
                                }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {
                                    Text("Room Number: ${room.roomNumber}")
                                    Text("Building: ${room.building.name}")
                                    Text("Operating System: ${room.getOperatingSystem()}")
                                    Text("Timeslots: ${room.timeSlots}")
                                    Text("Days of The Week Available: ${room.daysOfTheWeek}")
                                    // Add more details as needed: timeSlots, daysOfTheWeek, etc.
                                }
                            }

                        }
                    }
                }
                else {
                        Text("No rooms found in this building for that OS type")
                        Text("Please contact your administrator to add the room")
                    }
            }
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.BottomCenter)) {
                Button(onClick = {navigator.pop()}, Modifier.padding(horizontal = 2.dp)) {
                    Text("Return to Main Menu") }
            }
        }
    }

}