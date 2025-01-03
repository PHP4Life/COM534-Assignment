//////////////////// SummaryDailyBookingsView.kt //////////////////////////////////
///////////////////////// Author: Edward Kirr ///////////////////////////////////
////// Description: Displays the bookings for that building, room and day //////


package views.adminViews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

import logic.ComputerBooking


data class SummaryDailyBookingsView(val bookings: MutableList<ComputerBooking>?) : Screen {
    // Parameters:
    // MutableList<ComputerBooking> - If there are any bookings made, it will contain a list of computer booking data object

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        MaterialTheme {
            Column {
                Row() {
                    if (bookings?.isNotEmpty() == true) {
                        LazyColumn {
                            items(bookings) { booking ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .clickable {},
                                    onClick = {}
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(16.dp)
                                    ) {
                                        Text("Booked by: ${booking.student}")
                                        Text("Computer ID: ${booking.computerId}")
                                        Text("Day: ${booking.day}")
                                        Text("Time Slot: ${booking.timeSlot}")
                                    }
                                }

                            }
                        }
                    } else {
                        Text("No bookings found for this day")
                    }
                }
            }
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.BottomCenter)) {
                Button(onClick = {navigator.pop()}, Modifier.padding(horizontal = 2.dp)) {
                    Text("Return to Main Menu") }
            }
        }
    }
}