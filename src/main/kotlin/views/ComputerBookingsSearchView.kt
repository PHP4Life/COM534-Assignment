package views

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

import logic.Building
import logic.Room
import logic.University
import logic.User

data class ComputerBookingsSearchView(val user: User, val university: University) : Screen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val buildings = university.getBuildings()
        val expandBuilding = remember { mutableStateOf(false) }
        val selectedBuilding = remember {  mutableStateOf<Building?>(buildings[0]) }

        val rooms = selectedBuilding.value?.getRooms()
        val expandRoom = remember { mutableStateOf(false) }
        val selectedRoom = remember { mutableStateOf<Room?>(null) }

        val selectedDay = remember { mutableStateOf("Please Pick a Computer First") }

        val selectedTime = remember { mutableStateOf("Please Pick a Day First") }

        MaterialTheme {
            Column() {
                Row() { Text("Graphically View Booked Computers", fontWeight = FontWeight.Bold) }
                Row() { Text("Select a building: ") }
                Row() {
                    LaunchedEffect(selectedBuilding.value) {
                        selectedRoom.value = null
                    }
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
                            } else {
                                Text("Please pick a building before, if there are no buildings, contact your Administrator")
                            }
                        }
                    }
                }
                Row() {Text("Select a day: ")}
                Row(Modifier.horizontalScroll(rememberScrollState()).padding(top = 5.dp))
                {
                    if (selectedRoom.value != null) {
                        val daysOfTheWeek = selectedRoom.value!!.daysOfTheWeek
                        for (day in daysOfTheWeek) {
                            FilterChip(
                                selected = selectedDay.value.contains(day),
                                onClick = {
                                    selectedDay.value = if (selectedDay.value == day) "" else day
                                },
                                modifier = Modifier.padding(4.dp),
                                content = { Text(day) }
                            )
                        }
                    } else {
                        FilterChip(selected = false, onClick = {}, content = {Text("Select a Room beforehand")})
                    }
                }

                Row() { Text("Select a time slot: ") }
                Row(Modifier.horizontalScroll(rememberScrollState()).padding(top = 5.dp))
                {
                    if (selectedRoom.value != null) {
                        val timeSlots = selectedRoom.value!!.timeSlots
                        for (time in timeSlots) {
                            FilterChip(
                                selected = selectedTime.value.contains(time),
                                onClick = {
                                    selectedTime.value = if (selectedTime.value == time) "" else time
                                },
                                modifier = Modifier.padding(4.dp),
                                content = { Text(time) }
                            )
                        }
                    } else {
                        FilterChip(selected = false, onClick = {}, content = {Text("Select a Room beforehand")})
                    }
                }
                Row() { Button(onClick = {
                    if (selectedRoom.value != null) {
                        navigator.push(ComputerBookingsGraphicView(user,
                            selectedRoom.value!!, selectedDay.value, selectedTime.value))
                    }

                }) {Text("View Room")} }

            }
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.BottomCenter)) {
                Button(onClick = {navigator.pop()}, Modifier.padding(horizontal = 2.dp)) {
                    Text("Return to Main Menu") }}
        }
    }

}