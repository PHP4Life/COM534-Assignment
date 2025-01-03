////////////////////////// LoginOrRegisterView.kt ///////////////////////////////
///////////////////////// Author: Edward Kirr //////////////////////////////////
//// Description: Responsible for prompting the user to login or register /////

package views

import logic.UserAccounts

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.*



class LoginOrRegisterView : Screen {
    // The user chooses whether to log in or register.
    // This Flags a bool which changes the content of the next screen popped
    // Navigation is handled with the voyager library
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        MaterialTheme {
            Column(
                modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
                    horizontalArrangement = Arrangement.Center) {
                    Text("University Booking System", style = MaterialTheme.typography.h3) }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {navigator.push(LoginOrRegisterViewForm(false)) },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) { Text("Login") }
                    Button(
                        onClick = {navigator.push(LoginOrRegisterViewForm(true)) },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) { Text("Register") }
                }
            }
        }

    }
}


data class LoginOrRegisterViewForm(val register: Boolean) : Screen {
    // Based on the value of the register bool, the screen will display an extra field and access the UserAccounts()
    // signup method to create user. Otherwise, it retrieves a user based on the username and password,
    // if anything is returned, the user is logged in
    @Composable
    override fun Content() {
        val name = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val email = remember { mutableStateOf("") }
        var showDialog by remember { mutableStateOf(false) }
        val navigator = LocalNavigator.currentOrThrow

        MaterialTheme {
            Column(
                modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    val string = if (register) "Register" else "Login"
                    Text("$string: ", style = MaterialTheme.typography.h6)}
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) { TextField(
                        singleLine = true,
                        value = name.value,
                        onValueChange = { name.value = it },
                        modifier = Modifier.padding(vertical = 5.dp),
                        label = { Text("Name") }
                    )
                }
                Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) { TextField(
                    singleLine = true,
                    value = password.value,
                    onValueChange = { password.value = it },
                    modifier = Modifier.padding(vertical = 5.dp),
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation()
                )}
                if (register) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) { TextField(
                                singleLine = true,
                                value = email.value,
                                onValueChange = { email.value = it },
                                modifier = Modifier.padding(vertical = 5.dp),
                                label = { Text("Email") }
                            )
                        }
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            if (register && validateInput(name.value, password.value, email.value, register)) {
                                val createAccount =  UserAccounts().signUp(name.value, password.value, email.value)
                                    if (createAccount != null) {
                                        navigator.push(MainMenuView(createAccount))
                                    }
                                    else {showDialog = true}

                            } else if (!register && validateInput(name.value, password.value, email.value, register)){
                                val loggedIn = UserAccounts().getUsers().find {it.name == name.value && it.password == password.value}
                                if (loggedIn != null) {
                                    navigator.push(MainMenuView(loggedIn))
                                }
                                else {showDialog = true}
                            }
                            else {showDialog = true}
                        }
                    ) { Text("Proceed") }
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Error") },
                            text = { Text("Please check your input and try again.") },
                            buttons = {
                                Button(
                                    onClick = { showDialog = false }
                                ) {
                                    Text("OK")
                                }
                            }
                        )
                    }
                }
            }
            Column(Modifier.fillMaxSize().wrapContentSize(Alignment.BottomCenter))
            { Button(onClick = {navigator.popUntilRoot()}) {Text("Return")} }
        }

    }
    private fun validateInput(name: String, password: String, email: String, register: Boolean): Boolean {
        return name.isNotBlank() && password.isNotBlank() && (!register || email.isNotBlank())
    }
}




//
