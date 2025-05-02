package com.app.fabricexercise.ui.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.fabricexercise.R

@Composable
fun RegistrationScreen(viewModel: RegistrationViewModel) {
    val state by viewModel.state.collectAsState()
    val remainingTime by viewModel.remainingFormattedTime.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                RegistrationState.Initial -> {
                    Button(onClick = { viewModel.registerDevice() }) {
                        Text(text = stringResource(id = R.string.register_device))
                    }
                }

                RegistrationState.InProgress -> {
                    Text(text = "Time left: $remainingTime",
                        style = TextStyle(
                            color = Color.Blue,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.padding(20.dp))
                    Text(text = stringResource(id = R.string.registration_in_progress),
                        style = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                RegistrationState.Registered -> {
                    Text(
                        text = stringResource(id = R.string.registered_successfully),
                        style = TextStyle(
                            color = Color.Green,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                RegistrationState.RegistrationExpired -> {
                    Text(text = stringResource(id = R.string.registration_expired),
                        style = TextStyle(
                            color = Color.Blue,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ))
                }

                RegistrationState.RegistrationFailed -> {
                    Text(
                        text = stringResource(id = R.string.registration_failed),
                        style = TextStyle(
                            color = Color.Red,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
