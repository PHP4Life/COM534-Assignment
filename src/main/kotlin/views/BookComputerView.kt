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
import logic.ComputerBooking
import logic.Room
import logic.User

data class BookComputerView(val user: User, val room: Room) : Screen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val expandComputer = remember { mutableStateOf(false) }
        val selectedComputerString = remember { mutableStateOf("") }

        val expandDay = remember { mutableStateOf(false) }
        val selectedDay = remember { mutableStateOf("Please Pick a Computer First") }

        val expandTime = remember { mutableStateOf(false) }
        val selectedTime = remember { mutableStateOf("Please Pick a Day First") }

        val selectedComputerObject = room.findComputerByGlobalId(selectedComputerString.value)
        var showDialog by remember { mutableStateOf(false) }
        var showErrorDialog by remember { mutableStateOf(false) }

        MaterialTheme {
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
                Row() {Text("Select a Computer: ")}
                Row() {
                    ExposedDropdownMenuBox(expanded = expandComputer.value, onExpandedChange = {
                        expandComputer.value = !expandComputer.value
                    }) {
                        TextField(value = selectedComputerString.value, onValueChange = {}, readOnly = true, trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandComputer.value)
                        })
                        ExposedDropdownMenu(expanded = expandComputer.value,
                            onDismissRequest = { expandComputer.value = false }
                        ) {
                            for (computer in room.getComputers()) {
                                DropdownMenuItem(
                                    onClick = {
                                        // Handle item selection
                                        computer.getBookings()
                                        selectedComputerString.value = computer.toString()
                                        expandComputer.value = false
                                    }
                                ) { Text(computer.toString()) }

                            }
                        }
                    }
                }
                Row() { Text("Select a day: ") }
                Row() {
                    LaunchedEffect(selectedComputerObject) {
                        selectedDay.value = "Please Pick a Computer First"
                    }
                    ExposedDropdownMenuBox(expanded = expandDay.value, onExpandedChange = {
                        expandDay.value = !expandDay.value
                    }) {
                        TextField(value = selectedDay.value, onValueChange = {}, readOnly = true, trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandDay.value)
                        })
                        ExposedDropdownMenu(expanded = expandDay.value,
                            onDismissRequest = { expandDay.value = false }
                        ) {
                            if (selectedComputerObject != null) {
                                val daysOfTheWeek = selectedComputerObject.getAvailableBookingDates().keys
                                if (daysOfTheWeek.isNotEmpty()) {
                                    for (day in daysOfTheWeek) {
                                        DropdownMenuItem(
                                            onClick = {
                                                // Handle item selection
                                                selectedDay.value = day
                                                expandDay.value = false
                                            }
                                        ) { Text(day) }
                                    }
                                }
                                else { DropdownMenuItem(onClick = {}) { Text("Not available for any days") }}
                            }
                        }
                    }
                }
                Row() { Text("Select a time slot: ") }
                Row() {
                    LaunchedEffect(selectedDay) {
                        selectedTime.value = "Please Pick a Day First" // Reset selectedTime to null
                    }
                    // Responsible for handling if the user changes the day at the timeslot was picked
                    ExposedDropdownMenuBox(expanded = expandTime.value, onExpandedChange = {
                        expandTime.value = !expandTime.value
                    }) {
                        TextField(value = selectedTime.value, onValueChange = {}, readOnly = true, trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandTime.value)
                        })
                        ExposedDropdownMenu(expanded = expandTime.value,
                            onDismissRequest = { expandTime.value = false }
                        ) {
                            if (selectedComputerObject != null) {
                                val bookingDay = remember(selectedComputerObject, selectedDay) {
                                    selectedComputerObject.getAvailableBookingDates()
                                }
                                val timeSlots = bookingDay[selectedDay.value] ?: emptyList()
                                if (timeSlots.isNotEmpty()) {
                                    for (time in timeSlots) {
                                        DropdownMenuItem(
                                            onClick = {
                                                // Handle item selection
                                                selectedTime.value = time
                                                expandTime.value = false
                                            }
                                        ) { Text(time) }
                                    }
                                }
                                else { DropdownMenuItem(onClick = {}) { Text("No time slots available") }
                                }
                            }
                        }
                    }
                }

                Row() {
                    Column() {Button(onClick = {
                        val successfulBooking = selectedComputerObject?.addBooking(ComputerBooking(selectedComputerString.value, selectedDay.value, selectedTime.value, user.name))
                        if (successfulBooking == true) {
                            showDialog = true
                        } else {showErrorDialog = true}
                    }, Modifier.padding(horizontal = 2.dp)) { Text("Book Room") }}
                    if (showDialog) {
                            AlertDialog(
                                onDismissRequest = {navigator.popUntil { it is MainMenuView }},
                                text = { Text("Booking Successfully Made!") },
                                buttons = {
                                    Button(onClick = { navigator.popUntil { it is MainMenuView }}) {
                                        Text("Return To Main Menu")
                                    }
                                }
                            )
                    }
                    if (showErrorDialog) {
                        AlertDialog(
                            onDismissRequest = {navigator.popUntil { it is MainMenuView }},
                            text = { Column {
                                Text("Booking was Unsuccessful, please try again.")
                                Text("If the issue persists. Contact your Administrator")
                            }},
                            buttons = {
                                Button(onClick = { navigator.popUntil { it is MainMenuView }}) {
                                    Text("Return To Main Menu")
                                }
                            }
                        )
                    }
                }
            }
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.BottomCenter)) {
                Button(onClick = { navigator.pop() }, Modifier.padding(horizontal = 2.dp)) {
                    Text("Return to Room Viewer")
                }
            }
        }
    }
}