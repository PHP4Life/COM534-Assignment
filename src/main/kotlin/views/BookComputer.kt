package views

import androidx.compose.foundation.layout.*
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
import logic.Room
import logic.User

data class BookComputer(val user: User, val room: Room) : Screen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val expandTime = remember { mutableStateOf(false) }
        val selectedTime = remember { mutableStateOf("") }

        val expandDay = remember { mutableStateOf(false) }
        val selectedDay = remember { mutableStateOf("") }

        val expandComputer = remember { mutableStateOf(false) }
        val selectedComputer = remember { mutableStateOf("") }

        MaterialTheme {
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
//                Row() { Text("Select a day: ") }
//                Row() {
//                    ExposedDropdownMenuBox(expanded = expandDay.value, onExpandedChange = {
//                        expandDay.value = !expandDay.value
//                    }) {
//                        TextField(value = selectedDay.value, onValueChange = {}, readOnly = true, trailingIcon = {
//                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandDay.value)
//                        })
//                        ExposedDropdownMenu(expanded = expandDay.value,
//                            onDismissRequest = { expandDay.value = false }
//                        ) {
//                            for (day in room.daysOfTheWeek) {
//                                DropdownMenuItem(
//                                    onClick = {
//                                        // Handle item selection
//                                        selectedDay.value = day
//                                        expandDay.value = false
//                                    }
//                                ) { Text(day) }
//
//                            }
//                        }
//                    }
//                }
//                Row() { Text("Select a time slot: ") }
//                Row() {
//                    ExposedDropdownMenuBox(expanded = expandTime.value, onExpandedChange = {
//                        expandTime.value = !expandTime.value
//                    }) {
//                        TextField(value = selectedTime.value, onValueChange = {}, readOnly = true, trailingIcon = {
//                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandTime.value)
//                        })
//                        ExposedDropdownMenu(expanded = expandTime.value,
//                            onDismissRequest = { expandTime.value = false }
//                        ) {
//                            for (time in room.timeSlots) {
//                                DropdownMenuItem(
//                                    onClick = {
//                                        // Handle item selection
//                                        selectedTime.value = time
//                                        expandTime.value = false
//                                    }
//                                ) { Text(time) }
//
//                            }
//                        }
//                    }
//                }
                Row() {Text("Select a Computer: ")}
                Row() {
                    ExposedDropdownMenuBox(expanded = expandComputer.value, onExpandedChange = {
                        expandComputer.value = !expandComputer.value
                    }) {
                        TextField(value = selectedComputer.value, onValueChange = {}, readOnly = true, trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandComputer.value)
                        })
                        ExposedDropdownMenu(expanded = expandComputer.value,
                            onDismissRequest = { expandComputer.value = false }
                        ) {
                            for (computer in room.getComputers()) {
                                DropdownMenuItem(
                                    onClick = {
                                        // Handle item selection
                                        selectedComputer.value = computer.toString()
                                        expandComputer.value = false
                                    }
                                ) { Text(computer.toString()) }

                            }
                        }
                    }
                }

                Row() {
                    Column() {Button(onClick = {
                        val computer = room.findComputerByGlobalId(selectedComputer.value)
                        if (computer != null) {
                            println(computer.getAvailableBookingDates())
                        }

                    },
                        Modifier.padding(horizontal = 2.dp))  { Text("Book Room") }}
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