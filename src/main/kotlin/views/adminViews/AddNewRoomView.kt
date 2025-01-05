//////////////////// AddNewRoomView.kt //////////////////////////////////
/////////////////// Author: Edward Kirr ////////////////////////////////
////// Description: Prompts the user to select the building, //////////
//// enter a room number, OS type and number of computers ////////////
/// they are then navigated to the screen to add days and slots /////


package views.adminViews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import logic.Building

import logic.University

data class AddNewRoomView(val university: University) : Screen {
    // Parameters:
    // University - This acts as the main entry point, to access the buildings and its about classes
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val buildings = university.getBuildings()
        val navigator = LocalNavigator.currentOrThrow

        val expandBuilding = remember { mutableStateOf(false) }
        val selectedBuilding = remember {  mutableStateOf<Building?>(buildings[0]) }

        val roomNumber = remember { mutableStateOf("") }
        val isNewRoom = remember {mutableStateOf(true) }

        val expandOS = remember { mutableStateOf(false) }
        val operatingSystems = listOf("Windows", "Mac", "Linux")
        val selectedOS = remember {  mutableStateOf(operatingSystems[0]) }

        val numOfComputers = remember { mutableStateOf("") }
        val isMultipleOf5 = remember { mutableStateOf(false) }

        MaterialTheme {
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
                Row() { Text("Add a new room to a building", fontWeight = FontWeight.Bold) }
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
                Row() { Text("Room Number:") }
                Row() { TextField(
                    value = roomNumber.value,
                    onValueChange = { enteredRoomNumber ->
                        val num = enteredRoomNumber.toIntOrNull()
                        if (num != null) {
                            roomNumber.value = enteredRoomNumber
                            isNewRoom.value = true
                        } else {
                            roomNumber.value = "1"
                            isNewRoom.value = false}
                        },
                    isError = !isNewRoom.value,
                    modifier = Modifier.padding(vertical = 5.dp),
                    label = { Text("Must be unique to the building") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
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
                Row() {Text("Number Of Computers")}
                Row() { TextField(
                    value = numOfComputers.value,
                    onValueChange = { multipleOf5 ->
                        val num = multipleOf5.toIntOrNull()
                            if (num != null && num % 5 == 0 && num < 100) {
                                numOfComputers.value = multipleOf5
                                isMultipleOf5.value = true
                            } else {
                                numOfComputers.value = multipleOf5
                                isMultipleOf5.value = false}
                    },
                    modifier = Modifier.padding(vertical = 5.dp),
                    label = { Text("Must be multiples of 5") },
                    isError = !isMultipleOf5.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                }

                Row() { Button(onClick = {
                    // This approach takes a more proactive approach as I am explicitly checking if the variable is null or not
                    try {
                        val roomExists = selectedBuilding.value?.findRoomByNumber(roomNumber.value.toInt())
                        if (roomExists?.roomNumber == roomNumber.value.toIntOrNull()) {
                            isNewRoom.value = false
                        } else if (isNewRoom.value && isMultipleOf5.value && selectedBuilding.value != null) {
                            navigator.push(AddNewRoomSlotsView(selectedBuilding.value!!, roomNumber.value.toInt(), selectedOS.value, numOfComputers.value.toInt()))
                        }
                    } catch (_: NumberFormatException) {
                        isNewRoom.value = false
                    }

                }) {Text("Proceed to Days & Timeslots")} }

                if (!isNewRoom.value) {
                    AlertDialog(
                        onDismissRequest = {isNewRoom.value = true},
                        text = { Text("Please enter a room number that is not taken")},
                        buttons = {
                            Button(onClick = {isNewRoom.value = true}) {
                                Text("Return To Main Menu")
                            }
                        }
                    )
                }
            }
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.BottomCenter)) {
                Button(onClick = { navigator.pop() }, Modifier.padding(horizontal = 2.dp)) {
                    Text("Return to Main Menu")
                }
            }
        }
    }
}