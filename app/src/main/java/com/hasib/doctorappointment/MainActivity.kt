package com.hasib.doctorappointment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hasib.doctorappointment.data.AppData
import com.hasib.doctorappointment.ui.appointmentList.AppointmentListScreen
import com.hasib.doctorappointment.ui.theme.DoctorAppointmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoctorAppointmentTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = AppData.Routes.APPOINTMENT_LIST_SCREEN
                ) {
                    composable(AppData.Routes.APPOINTMENT_LIST_SCREEN) {
                        AppointmentListScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DoctorAppointmentTheme {
        Greeting("Android")
    }
}