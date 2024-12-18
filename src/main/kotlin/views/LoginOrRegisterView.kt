package views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.*
import logic.UserAccounts


class LoginOrRegisterView : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        MaterialTheme {
            Column(
                modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
            ) {
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
    @Composable
    override fun Content() {
        val name = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val email = remember { mutableStateOf("") }
        var showErrorDialog by remember { mutableStateOf(false) }
        val navigator = LocalNavigator.currentOrThrow

        MaterialTheme {
            Column(
                modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) { TextField(
                        value = name.value,
                        onValueChange = { name.value = it },
                        modifier = Modifier.padding(vertical = 5.dp),
                        label = { Text("Name") }
                    )
                }
                Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) { TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    modifier = Modifier.padding(vertical = 5.dp),
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation()
                )}
                if (register) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) { TextField(
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

                            } else if (!register && validateInput(name.value, password.value, email.value, register)){
                                val loggedIn = UserAccounts().getUsers().find {it.name == name.value && it.password == password.value}
                                if (loggedIn != null) {
                                    navigator.push(MainMenuView(loggedIn))
                                }
                                else {showErrorDialog = true}
                            }
                            else {showErrorDialog = true}
                        }
                    ) { Text("Proceed") }
                    if (showErrorDialog) {
                        AlertDialog(
                            onDismissRequest = { showErrorDialog = false },
                            title = { Text("Error") },
                            text = { Text("Please check your input and try again.") },
                            buttons = {
                                Button(
                                    onClick = { showErrorDialog = false }
                                ) {
                                    Text("OK")
                                }
                            }
                        )
                    }
                }
            }
        }

    }
    private fun validateInput(name: String, password: String, email: String, register: Boolean): Boolean {
        return name.isNotBlank() && password.isNotBlank() && (!register || email.isNotBlank())
    }
}




// https://developer.android.com/topic/libraries/architecture/livedata
