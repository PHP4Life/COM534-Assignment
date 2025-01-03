////////////////////////////// App.kt //////////////////////////////////////////
///////////////////////// Author: Edward Kirr /////////////////////////////////
////////// Description: Renders the composable frame, ////////////////////////
///////////// here global theming can be applied ////////////////////////////


import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import views.LoginOrRegisterView

@Composable
fun App() {
    MaterialTheme {
        Navigator(LoginOrRegisterView()) { navigator ->
            SlideTransition(navigator)
        }
    }
}