/////////////////////// ComputerBookingGraphicView.kt /////////////////////
///////////////////////// Author: Edward Kirr ////////////////////////////
//// Description: Based on the room, day and time slot selected, ////////
// a grid view will be displayed with the different computers  /////////
/////////// Purple = Booked by logged-in user /////////////////////////
/////////////////// Red = Booked by another user /////////////////////
///////////////// Green = Available /////////////////////////////////

package views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import logic.Computer

import logic.ComputerBooking

import logic.Room
import logic.User


data class ComputerBookingsGraphicView(val user: User, val room: Room, val day: String, val time: String) : Screen {

    @Composable
    override fun Content() {
        val computers = room.getComputers()
        val navigator = LocalNavigator.currentOrThrow

        val selectedBooking = remember { mutableStateOf<ComputerBooking?>(null) }
        val selectedComputer = remember { mutableStateOf<Computer?>(null) }

        val createBooking = remember { mutableStateOf(false) }
        val deleteBooking = remember { mutableStateOf(false) }
        val viewBooking = remember { mutableStateOf(false) }

        MaterialTheme {
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
                computers.chunked(5).forEach { rowComputers ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 15.dp)) {
                        rowComputers.forEach { computer ->
                            computer.getBookings()
                            val computerBooking = computer.getComputerBookingByDateTime(day, time)
                            val booking = computerBooking.firstOrNull()
                            val backgroundColor = when {
                                computerBooking.isNotEmpty() && computerBooking[0]?.student == user.name -> Color.Magenta // Booked by logged-in user
                                // Had to use magenta as the color object doesn't have a purple property, could use Hex code instead
                                computerBooking.isNotEmpty() -> Color.Red // Other booking
                                else -> Color.Green // Unreserved
                            }
                            Column(
                                modifier = Modifier
                                    .size(100.dp)
                                    .background(backgroundColor)
                                    .clickable {selectedComputer.value = computer
                                        selectedBooking.value = booking
                                        when {
                                            booking == null -> createBooking.value = true
                                            booking.student == user.name -> deleteBooking.value = true
                                            user.getUserType() == "Admin" -> viewBooking.value = true
                                        } }
                                    .padding(8.dp),
                            ) {
                                Icon(
                                    Icons.Filled.AccountCircle,
                                    modifier = Modifier.size(60.dp),
                                    contentDescription = computer.globalId,
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = computer.globalId)
                            }
//                            Icon(
//                                Icons.Filled.AccountCircle,
//                                contentDescription = "Computer ${computer.globalId}",
//                                modifier = Modifier
//                                    .size(100.dp)
//                                    .background(backgroundColor)
//                                    .clickable {
//                                        selectedComputer.value = computer
//                                        selectedBooking.value = booking
//                                        when {
//                                            booking == null -> createBooking.value = true
//                                            booking.student == user.name -> deleteBooking.value = true
//                                            user.getUserType() == "Admin" -> viewBooking.value = true
//                                        }
//                                    }
//                                    .padding(8.dp), // Add padding inside the Icon
//                                tint = Color.White
//                            )
                        }
                    }
                }
                if (createBooking.value) {
                    AlertDialog(
                        onDismissRequest = { createBooking.value = false},
                        title = { Text("Create Booking?")},
                        text = { Column {
                            Text("Do you want to Book ${selectedComputer.value?.globalId}")
                            Text("Day: $day")
                            Text("Time: $time")
                        }},
                        buttons = {
                            Button(onClick = {createBooking.value = false}) {
                                Text("No")
                            }
                            Button(onClick = { if (selectedComputer.value?.addBooking(ComputerBooking(selectedComputer.value!!.globalId, day, time, user.name)) == true) {createBooking.value = false} }) {
                                Text("Yes")
                            }
                        }
                    )
                }
                if (deleteBooking.value) {
                    AlertDialog(
                        onDismissRequest = { deleteBooking.value = false },
                        title = { Text("Cancel Booking?") },
                        text = { Column {
                            Text("Are you sure you want to cancel: ")
                            Text("Computer: ${selectedComputer.value?.globalId}")
                            Text("Day: $day")
                            Text("Slot: $time")
                            Text("If Yes, your slot will be made available for others to book")
                        }},
                        buttons = {
                            Button(
                                onClick = {deleteBooking.value = false },
                                modifier = Modifier.padding(horizontal = 16.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White)) {
                                Text("No, Do not Cancel") }
                            Button(
                                onClick = {
                                    if (selectedBooking.value != null) {
                                        if (selectedComputer.value?.deleteBooking(selectedBooking.value!!) == true)
                                            navigator.pop()
                                    }
                                },
                                modifier = Modifier.padding(horizontal = 16.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green, contentColor = Color.White)) {
                                Text("Yes, Cancel Booking") }
                        }
                    )
                }
                if (viewBooking.value) {
                    AlertDialog(
                        onDismissRequest = { viewBooking.value = false },
                        title = { Text("View Booking for ${selectedBooking.value?.student}") },
                        text = { Text("Booking for ${selectedComputer.value?.globalId} on $day for slot $time") },
                        buttons = { Button(onClick = {viewBooking.value = false}) { Text("Close") } }
                    )
                }
            }
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.BottomCenter)) {
                Button(onClick = {navigator.pop()}, Modifier.padding(horizontal = 2.dp)) {
                    Text("Return to Main Menu") }
            }
        }
    }
}