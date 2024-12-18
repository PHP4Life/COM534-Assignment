package views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import logic.University
import logic.User

data class SearchForRoomView(val user: User, val university: University, val bookRoom: Boolean): Screen {

    // Had to use the experimental material UI class as the ExposedMenuBox was not implemented fully.
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val expandBuilding = remember { mutableStateOf(false) }
        val expandOS = remember { mutableStateOf(false) }

        val buildings = university.getBuildings()
        val selectedBuilding = remember { mutableStateOf("") }

        // This is a limitation as if there is another OS to be added i.e. OpenBSD, this has to be programmatically added
        val operatingSystems = listOf("Windows", "Mac", "Linux")
        val selectedSystem = remember { mutableStateOf("") }
        MaterialTheme {
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
                Row() { }
                Row() { Text("Search for Rooms") }
                Row() { Text("Select a building: ") }
                Row() {
                    ExposedDropdownMenuBox(expanded = expandBuilding.value, onExpandedChange = {
                        expandBuilding.value = !expandBuilding.value
                    }) {
                        TextField(value = selectedBuilding.value, onValueChange = {}, readOnly = true, trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandBuilding.value)
                        })
                        ExposedDropdownMenu(expanded = expandBuilding.value,
                            onDismissRequest = { expandBuilding.value = false }
                        ) {
                            for (building in buildings) {
                                DropdownMenuItem(
                                    onClick = {
                                        // Handle item selection
                                        selectedBuilding.value = building.name
                                        expandBuilding.value = false
                                    }
                                ) { Text(building.name) }
                            }
                        }
                    }
                }
                Row() { Text("Select an operating system: ") }
                Row() {
                    ExposedDropdownMenuBox(expanded = expandOS.value, onExpandedChange = {
                        expandOS.value = !expandOS.value
                    }) {
                        TextField(value = selectedSystem.value, onValueChange = {}, readOnly = true, trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandOS.value)
                        })
                        ExposedDropdownMenu(expanded = expandOS.value,
                            onDismissRequest = { expandOS.value = false }
                        ) {
                            for (system in operatingSystems) {
                                DropdownMenuItem(
                                    onClick = {
                                        // Handle item selection
                                        selectedSystem.value = system
                                        expandOS.value = false
                                    }
                                ) { Text(system) }

                            }
                        }
                    }
                }
                Row() {
                    Column() {Button(onClick = {
                        val building = university.findBuildingByName(selectedBuilding.value)
                        val rooms = building?.getRooms()?.filter { it.getOperatingSystem() == selectedSystem.value }
                        if (rooms != null) {
                            navigator.push(RoomView(rooms, bookRoom))
                            println("This is the room you have filter")
                        }
                        else { print("no rooms found") }
                   },
                        Modifier.padding(horizontal = 2.dp))  { Text("Search Rooms") }}
                }

            }
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.BottomCenter)) {
                Button(onClick = {navigator.pop()}, Modifier.padding(horizontal = 2.dp)) {
                    Text("Return to Main Menu") }}
        }
    }
}