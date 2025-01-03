//////////////////// SearchDailyBookingsView.kt //////////////////////////////////
///////////////////////// Author: Edward Kirr ///////////////////////////////////
/////////// Description: Allows the user to pick a building, room and day //////
/////// which returns a list of bookings made. Only admin accessible  /////////

package views.adminViews

import logic.Building
import logic.Room
import logic.University

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

data class SearchDailyBookingsView(val university: University) : Screen {
    // Parameters:
    // University - takes in a university object to get the buildings and its room
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val buildings = university.getBuildings()
        val navigator = LocalNavigator.currentOrThrow

        val expandBuilding = remember { mutableStateOf(false) }
        val selectedBuilding = remember {  mutableStateOf<Building?>(buildings[0]) }

        val rooms = selectedBuilding.value?.getRooms()
        val expandRoom = remember { mutableStateOf(false) }
        val selectedRoom = remember { mutableStateOf<Room?>(null) }

        val days = selectedRoom.value?.daysOfTheWeek
        val expandDay = remember { mutableStateOf(false) }
        val selectedDay = remember { mutableStateOf("Select a day...") }

// TODO: Tried to use generics to create custom expanded drop down but due to the extra logic and checks in the room drop down, this would add a lot of complexity
        MaterialTheme {
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
                Row() { Text("Search for Bookings by Day & Room", fontWeight = FontWeight.Bold) }
                Row() { Text("Select a building: ") }
                Row() {
                    ExposedDropdownMenuBox(expanded = expandBuilding.value, onExpandedChange = {
                        expandBuilding.value = !expandBuilding.value
                    }) {
                        TextField(value = selectedBuilding.value.toString(), onValueChange = {}, readOnly = true, trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandBuilding.value)
                        })
                        ExposedDropdownMenu(expanded = expandBuilding.value,
                            onDismissRequest = { expandBuilding.value = false }
                        ) {
                            for (building in buildings) {
                                DropdownMenuItem(
                                    onClick = {
                                        selectedRoom.value = null
                                        selectedBuilding.value = building
                                        expandBuilding.value = false
                                    }
                                ) { Text(building.name) }
                            }
                        }
                    }
                }
                Row() { Text("Select a Room: ") }
                Row() {
                    ExposedDropdownMenuBox(expanded = expandRoom.value, onExpandedChange = {
                        expandRoom.value = !expandRoom.value
                    }) {
                        TextField(value = selectedRoom.value?.roomNumber?.toString() ?: "Select a Room...", onValueChange = {}, readOnly = true, trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandRoom.value)
                        })
                        ExposedDropdownMenu(expanded = expandRoom.value,
                            onDismissRequest = { expandRoom.value = false }
                        ) {
                            if (rooms != null) {
                                for (room in rooms) {
                                    DropdownMenuItem(
                                        onClick = {
                                            // Handle item selection
                                            selectedRoom.value = room
                                            expandRoom.value = false
                                        }
                                    ) { Text(room.roomNumber.toString()) }
                                }
                            } else {Text("Please pick a building before, if there are no buildings, contact your Administrator")}
                        }
                    }
                }
                Row() { Text("Select a Day: ") }
                Row() {
                    ExposedDropdownMenuBox(expanded = expandDay.value, onExpandedChange = {
                        expandDay.value = !expandDay.value
                    }) {
                        TextField(value = selectedDay.value, onValueChange = {}, readOnly = true, trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandDay.value)
                        })
                        ExposedDropdownMenu(expanded = expandDay.value,
                            onDismissRequest = { expandDay.value = false }
                        ) {
                            if (days != null) {
                                for (day in days) {
                                    DropdownMenuItem(
                                        onClick = {
                                            // Handle item selection
                                            selectedDay.value = day
                                            expandDay.value = false
                                        }
                                    ) { Text(day) }
                                }
                            } else {Text("Note: Please pick a room before picking a day")}
                        }
                    }
                }
                Row () { Button(onClick = {
                    val bookings = selectedRoom.value?.getBookingsByDay(selectedDay.value)
                    navigator.push(SummaryDailyBookingsView(bookings))
                }) {
                    Text("Begin Search") }
                }
            }
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.BottomCenter)) {
                Button(onClick = {navigator.pop()}, Modifier.padding(horizontal = 2.dp)) {
                    Text("Return to Main Menu") }}
        }
    }
}