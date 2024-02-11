@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.hasib.doctorappointment.screens.appointmentList

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hasib.doctorappointment.R
import com.hasib.doctorappointment.model.AppointmentItem
import com.hasib.doctorappointment.model.SlotStatus
import com.hasib.doctorappointment.model.chipColor
import com.hasib.doctorappointment.ui.theme.DoctorAppointmentTheme
import com.hasib.doctorappointment.utils.Resources
import com.hasib.doctorappointment.utils.toFormattedString
import java.time.LocalTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppointmentListScreen(
    uiState: AppointmentListState,
    loadAppointments: (query: String, status: String) -> Unit,
    bookAppointment: (appointmentId: String) -> Unit,
    resetViewModel: () -> Unit,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        loadAppointments("", SlotStatus.AVAILABLE.name)
    }

    LaunchedEffect(key1 = uiState.bookingStatus) {
        if (uiState.bookingStatus) {
            Toast.makeText(
                context,
                "Appointment Booked Successfully",
                Toast.LENGTH_SHORT
            ).show()
        }
        resetViewModel()
    }

    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Doctor Appointment",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    actions = {
                        IconButton(onClick = onSignOut) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "Logout",
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                )
            }
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                when (uiState.appointments) {
                    is Resources.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.Center)
                        )
                    }

                    is Resources.Error -> {
                        Text(
                            text = uiState.appointments.throwable?.localizedMessage
                                ?: "Error"
                        )
                    }

                    is Resources.Success -> {
                        AppointmentList(
                            searchString = uiState.query,
                            appointmentItems = uiState.appointments.data!!,
                            loadAppointments = loadAppointments,
                            bookAppointment = bookAppointment
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchString: String,
    onQueryChange: (String) -> Unit = {}
) {
    Box(modifier = modifier) {
        TextField(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Doctor"
                )
            },
            value = searchString,
            onValueChange = {
                onQueryChange(it)
            },
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant),
            label = { Text("Search Doctor") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
        )
    }
}

@Composable
fun AppointmentList(
    searchString: String,
    appointmentItems: List<AppointmentItem>,
    loadAppointments: (query: String, status: String) -> Unit,
    bookAppointment: (appointmentId: String) -> Unit
) {
    val selectedFilter = remember {
        mutableStateOf(SlotStatus.AVAILABLE.name)
    }

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        searchString = searchString
    ) {
        loadAppointments(it, selectedFilter.value)
    }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        val filters: List<String> = listOf("ALL").plus(SlotStatus.entries.map { it.name })
        items(filters) { status ->
            FilterChip(
                selected = status == selectedFilter.value,
                onClick = {
                    selectedFilter.value = status
                    loadAppointments(searchString, status)
                },
                label = { Text(text = status.replace('_', ' ')) }
            )
        }
    }
    Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(bottom = 4.dp))
    LazyColumn {
        items(appointmentItems) { appointment ->
            AppointmentItem(
                appointment = appointment,
                bookAppointment = bookAppointment
            )
        }
    }
}

@Composable
fun AppointmentItem(
    appointment: AppointmentItem,
    bookAppointment: (appointmentId: String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = Color.Black
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp)
        ) {
            Column {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Doctor Name",
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = appointment.doctorName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Slot Time",
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = appointment.time.toFormattedString(),
                            fontSize = 20.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                AssistChip(
                    onClick = { },
                    label = { Text(text = appointment.status.name.replace('_', ' ')) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = appointment.status.chipColor(),
                        labelColor = when (appointment.status) {
                            SlotStatus.BOOKED, SlotStatus.FillED_UP -> Color.White
                            else -> Color.Black
                        }
                    ),
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "${appointment.available}/${appointment.total}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(32.dp))
                ElevatedButton(
                    enabled = appointment.status == SlotStatus.AVAILABLE,
                    onClick = {
                        bookAppointment(appointment.id)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_calendar_add_on_24),
                        contentDescription = "Book Appointment"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppointmentListScreenPreview() {
    DoctorAppointmentTheme {
        AppointmentListScreen(
            uiState = AppointmentListState(
                appointments = Resources.Success(
                    data = listOf(
                        AppointmentItem(
                            id = "1",
                            doctorName = "Dr. John Doe",
                            time = LocalTime.of(10, 0),
                            status = SlotStatus.AVAILABLE,
                            total = 10,
                            available = 5
                        )
                    )
                )
            ),
            loadAppointments = { _, _ -> },
            bookAppointment = { },
            resetViewModel = { },
            onSignOut = { }
        )
    }
}