package com.app.fabricexercise.ui.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
                        Text("Register Device")
                    }
                }
                
                RegistrationState.InProgress -> {
                    Text(text = "Time left: $remainingTime")
                    Text(text = "Registration in progress...")
                }

                RegistrationState.Registered -> {
                    Text(text = "Registered Successfully!")
                }

                RegistrationState.RegistrationExpired ->{
                    Text("Registration Expired!")
                }
                RegistrationState.RegistrationFailed -> {
                    Text("Registration Failed!")
                }
            }
        }
    }
}
