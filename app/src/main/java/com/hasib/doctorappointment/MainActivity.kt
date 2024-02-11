package com.hasib.doctorappointment

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.hasib.doctorappointment.screens.appointmentList.AppointmentListScreen
import com.hasib.doctorappointment.screens.appointmentList.AppointmentListViewModel
import com.hasib.doctorappointment.screens.sign_in.SignInScreen
import com.hasib.doctorappointment.screens.sign_in.SignInViewModel
import com.hasib.doctorappointment.services.GoogleAuthClient
import com.hasib.doctorappointment.ui.theme.DoctorAppointmentTheme
import com.hasib.doctorappointment.utils.Screen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val googleAuthClient by lazy {
        GoogleAuthClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoctorAppointmentTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.SignInScreen.route
                ) {
                    composable(
                        Screen.SignInScreen.route,
                        enterTransition = {
                            return@composable fadeIn(tween(1000))
                        },
                        exitTransition = {
                            return@composable slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                            )
                        },
                        popEnterTransition = {
                            return@composable slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                            )
                        },
                        popExitTransition = {
                            return@composable slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                            )
                        },
                    ) {
                        val viewModel: SignInViewModel = hiltViewModel()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        LaunchedEffect(key1 = Unit) {
                            if (googleAuthClient.getSignedInUser() != null) {
                                navController.navigate(Screen.AppointmentListScreen.route) {
                                    popUpTo(Screen.SignInScreen.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        }

                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult(),
                            onResult = { result ->
                                if (result.resultCode == RESULT_OK) {
                                    lifecycleScope.launch {
                                        val signInResult = googleAuthClient.signInWithIntent(
                                            intent = result.data ?: return@launch
                                        )
                                        viewModel.onSignInResult(signInResult)
                                    }
                                }
                            }
                        )

                        LaunchedEffect(key1 = state.isSignInSuccessful) {
                            if (state.isSignInSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Sign in successful",
                                    Toast.LENGTH_LONG
                                ).show()

                                navController.navigate(Screen.AppointmentListScreen.route) {
                                    popUpTo(Screen.SignInScreen.route) {
                                        inclusive = true
                                    }
                                }
                                viewModel.resetState()
                            }
                        }

                        SignInScreen(
                            state = state,
                            onSignInClick = {
                                lifecycleScope.launch {
                                    val signInIntentSender = googleAuthClient.signIn()
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInIntentSender ?: return@launch
                                        ).build()
                                    )
                                }
                            }
                        )
                    }
                    composable(
                        Screen.AppointmentListScreen.route,
                        enterTransition = {
                            return@composable slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                            )
                        },
                        popEnterTransition = {
                            return@composable slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                            )
                        },
                        popExitTransition = {
                            return@composable slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                            )
                        },
                    ) {
                        val viewModel: AppointmentListViewModel = hiltViewModel()

                        AppointmentListScreen(
                            uiState = viewModel.uiState,
                            loadAppointments = { query, status ->
                                viewModel.loadAppointments(query, status)
                            },
                            bookAppointment = { appointmentId ->
                                viewModel.bookAppointment(appointmentId)
                            },
                            resetViewModel = {
                                viewModel.resetState()
                            }
                        )
                    }
                }
            }
        }
    }
}
