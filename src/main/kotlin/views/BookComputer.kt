package views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import logic.Room

data class BookComputer(val room: Room) : Screen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val expandTime = remember { mutableStateOf(false) }
        val selectedTime = remember { mutableStateOf("") }

        val expandDay = remember { mutableStateOf(false) }
        val selectedDay = remember { mutableStateOf("") }

        MaterialTheme {
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
                Row() {ExposedDropdownMenuBox(expanded = expandTime.value, onExpandedChange = {
                    expandTime.value = !expandTime.value
                }) {
                    TextField(value = selectedTime.value, onValueChange = {}, readOnly = true, trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandTime.value)
                    })
                    ExposedDropdownMenu(expanded = expandTime.value,
                        onDismissRequest = { expandTime.value = false }
                    ) {
                        for (time in room.timeSlots) {
                            DropdownMenuItem(
                                onClick = {
                                    // Handle item selection
                                    selectedTime.value = time
                                    expandTime.value = false
                                }
                            ) { Text(time) }

                        }
                    }
                }}
                Row() {ExposedDropdownMenuBox(expanded = expandDay.value, onExpandedChange = {
                    expandDay.value = !expandDay.value
                }) {
                    TextField(value = selectedDay.value, onValueChange = {}, readOnly = true, trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandDay.value)
                    })
                    ExposedDropdownMenu(expanded = expandDay.value,
                        onDismissRequest = { expandDay.value = false }
                    ) {
                        for (day in room.daysOfTheWeek) {
                            DropdownMenuItem(
                                onClick = {
                                    // Handle item selection
                                    selectedDay.value = day
                                    expandDay.value = false
                                }
                            ) { Text(day) }

                        }
                    }
                }}
            }
        }
    }
}