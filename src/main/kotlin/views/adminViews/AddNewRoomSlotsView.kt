package views.adminViews

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

import logic.Building
import logic.Room
import views.MainMenuView


data class AddNewRoomSlotsView(val building: Building, val roomNum: Int, val os: String, val numOfComputers: Int) : Screen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
        val selectedDays = remember { mutableStateOf(mutableSetOf<String>()) }

        val times = listOf("9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm")
        val selectedTimes = remember { mutableStateOf(mutableSetOf<String>()) }

        val createdRoom = remember {mutableStateOf<Room?>(null) }

        MaterialTheme {
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
                Row() {Text("Select Days: ", fontWeight = FontWeight.Bold)}
                Row(Modifier.horizontalScroll(rememberScrollState()).padding(top = 5.dp))
                {
                   for (day in days) {
                        FilterChip(
                            selected = selectedDays.value.contains(day),
                            onClick = {
                                val newDays = selectedDays.value.toMutableSet()
                                if (newDays.contains(day)) {
                                    newDays.remove(day)
                                } else {
                                    newDays.add(day)
                                }
                                selectedDays.value = newDays
                            },
                            modifier = Modifier.padding(4.dp),
                            content = { Text(day) }
                        )
                    }
                }
                Row() {Text("Selected Days: ${selectedDays.value.joinToString()}")}
                Row(Modifier.padding(top = 10.dp)) {Text("Select Time Slots: ", fontWeight = FontWeight.Bold)}
                Row(Modifier.horizontalScroll(rememberScrollState()).padding(top = 5.dp))
                {
                    for (time in times) {
                        FilterChip(
                            selected = selectedTimes.value.contains(time),
                            onClick = {
                                val newTimes = selectedTimes.value.toMutableSet()
                                if (newTimes.contains(time)) {
                                    newTimes.remove(time)
                                } else {
                                    newTimes.add(time)
                                }
                                selectedTimes.value = newTimes
                            },
                            modifier = Modifier.padding(4.dp),
                            content = { Text(time) }
                        )
                    }
                }
                Row() {Text("Selected Time Slots: ${selectedTimes.value.joinToString()}")}
                Row() { Button(onClick =
                    { if (selectedDays.value.isNotEmpty() && selectedTimes.value.isNotEmpty()) {
                        when (os) {
                            "Windows" -> createdRoom.value = building.createWindowsRoom(roomNum, selectedTimes.value.toList(), selectedDays.value.toList(), numOfComputers)
                            "Mac" -> createdRoom.value = building.createMacRoom(roomNum, selectedTimes.value.toList(), selectedDays.value.toList(), numOfComputers)
                            "Linux" -> createdRoom.value = building.createLinuxRoom(roomNum, selectedTimes.value.toList(), selectedDays.value.toList(), numOfComputers)
                            }
                        if (createdRoom.value != null) {
                            building.addRoomToDB(createdRoom.value!!)
                            navigator.popUntil { it is MainMenuView }

                            }
                        }
                    }) { Text("Create Room") }
                }
            }
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.BottomCenter)) {
                Button(onClick = { navigator.pop() }, Modifier.padding(horizontal = 2.dp)) {
                    Text("Return to Room Editor")
                }
            }
        }
    }
}