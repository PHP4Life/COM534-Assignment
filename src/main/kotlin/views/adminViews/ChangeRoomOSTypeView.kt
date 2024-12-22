package views.adminViews

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

import logic.Building
import logic.Room
import logic.University

data class ChangeRoomOSTypeView(val university: University) : Screen {

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

        val expandOS = remember { mutableStateOf(false) }
        val operatingSystems = listOf("Windows", "Mac", "Linux")
        val selectedOS = remember {  mutableStateOf(operatingSystems[0]) }

        val confirmationDialog = remember { mutableStateOf(false) }


        MaterialTheme {
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
                Row() { Text("Change Operating System Type of Room", fontWeight = FontWeight.Bold) }
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
                Row() {Text("Operating System Type: ")}
                Row() {
                    ExposedDropdownMenuBox(expanded = expandOS.value, onExpandedChange = {
                        expandOS.value = !expandOS.value
                    }) {
                        TextField(value = selectedOS.value, onValueChange = {}, readOnly = true, trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandOS.value)
                        })
                        ExposedDropdownMenu(expanded = expandOS.value,
                            onDismissRequest = { expandOS.value = false }
                        ) {
                            for (os in operatingSystems) {
                                DropdownMenuItem(
                                    onClick = {
                                        selectedOS.value = os
                                        expandOS.value = false
                                    }
                                ) { Text(os) }
                            }
                        }
                    }
                }
                Row() { Button(onClick = {
                    if (selectedRoom.value != null) {
                        confirmationDialog.value = true
                    }
                }) {Text("Update OS Type for Room")} }

                }
            }
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.BottomCenter)) {
                Button(onClick = {navigator.pop()}, Modifier.padding(horizontal = 2.dp)) {
                    Text("Return to Main Menu") }}
            if (confirmationDialog.value) {
                AlertDialog(
                    onDismissRequest = { confirmationDialog.value = false },
                    title = { Text("Change OS?") },
                    text = { Column() {
                        Text("Are you sure you want to change the OS type: ")
                        Text("Building: ${selectedBuilding.value}")
                        Text("Room: ${selectedRoom.value?.roomNumber}")
                        Text("Current OS: ${selectedRoom.value?.getOperatingSystem()}")
                        Text("New OS ${selectedOS.value}")
                    }},
                    buttons = {
                        Button(
                            onClick = {confirmationDialog.value = false },
                            modifier = Modifier.padding(horizontal = 16.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White)) {
                            Text("No, Do not change") }
                        Button(
                            onClick = {
                                    val successfulUpdate = selectedBuilding.value?.updateRoomType(selectedRoom.value!!, selectedOS.value)
                                    if (successfulUpdate == true) {
                                        navigator.pop()
                                    }
                            },
                            modifier = Modifier.padding(horizontal = 16.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green, contentColor = Color.White)) {
                            Text("Yes, Cancel Booking") }
                    }
                )
            }
        }
    }