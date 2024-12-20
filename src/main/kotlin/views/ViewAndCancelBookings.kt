package views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

import logic.ComputerBooking
import logic.User

data class ViewAndCancelBookings(val user: User) : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val userBookings = user.getUserBookings()
        val selectedBooking = remember { mutableStateOf<ComputerBooking?>(null) }
        var showDialog by remember { mutableStateOf(false) }
        val selectedBookingComputer = remember { mutableStateOf("") }
        val selectedBookingDay = remember { mutableStateOf("") }
        val selectedBookingTime = remember { mutableStateOf("") }

        MaterialTheme {
            Column() {
                Row() {Text("Booked Computers: ", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))}
                Row() {
                    if (userBookings.isNotEmpty()) {
                        LazyColumn {
                            items(userBookings) { booking ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .clickable {},
                                    onClick = {
                                        selectedBooking.value = booking
                                        selectedBookingComputer.value = booking.computerId
                                        selectedBookingDay.value = booking.day
                                        selectedBookingTime.value = booking.timeSlot
                                        showDialog = true
                                    }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(16.dp)
                                    ) {
                                        Text("Computer ID: ${booking.computerId}")
                                        Text("Day: ${booking.day}")
                                        Text("Time Slot: ${booking.timeSlot}")
                                    }
                                }

                            }
                        }
                    } else {
                        Text("No bookings found under username: ${user.name}")
                        Text("If you believe you have made a booking, please contact your administrator")
                    }
                }
            }
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.BottomCenter)) {
                Button(onClick = {navigator.pop()}, Modifier.padding(horizontal = 2.dp)) {
                    Text("Return to Main Menu") }
            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Cancel Booking?") },
                    text = { Column() {
                        Text("Are you sure you want to cancel: ")
                        Text("Computer: ${selectedBookingComputer.value}")
                        Text("Day: ${selectedBookingDay.value}")
                        Text("Slot: ${selectedBookingTime.value}")
                        Text("If Yes, your slot will be made available for others to book")
                           }},
                    buttons = {
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier.padding(horizontal = 16.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White)) {
                            Text("No, Do not Cancel") }
                        Button(
                            onClick = {
                                if (selectedBooking.value != null) {
                                    val successfulDelete = user.deleteUserBooking(selectedBooking.value!!)
                                    if (successfulDelete) {
                                        navigator.pop()
                                    }

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

}