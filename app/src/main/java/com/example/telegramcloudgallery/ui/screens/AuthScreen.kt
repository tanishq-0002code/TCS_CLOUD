package com.example.telegramcloudgallery.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.telegramcloudgallery.data.telegram.AuthStep
import com.example.telegramcloudgallery.data.telegram.TelegramAuthRepository

/**
 * AuthScreen - Fully wired to TelegramAuthRepository.
 * Handles phone, OTP/code, and 2FA password states.
 */
@Composable
fun AuthScreen(
    authRepository: TelegramAuthRepository,
    onAuthSuccess: () -> Unit = {}
) {
    val uiState by authRepository.authUiState.collectAsStateWithLifecycle()
    val isLoggedIn by authRepository.isLoggedIn.collectAsStateWithLifecycle()

    var phoneNumber by rememberSaveable { mutableStateOf("+") }
    var authCode by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val codeFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    LaunchedEffect(uiState.step) {
        when (uiState.step) {
            AuthStep.WAIT_CODE -> codeFocusRequester.requestFocus()
            AuthStep.WAIT_PASSWORD -> passwordFocusRequester.requestFocus()
            AuthStep.READY -> onAuthSuccess()
            else -> {}
        }
    }

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onAuthSuccess()
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Connect Telegram") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = "Sign in with your Telegram account",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = "This uses Telegram's official API via TDLib. Your credentials never leave your device.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    when (uiState.step) {
                        AuthStep.INITIALIZING -> {
                            Text("Initializing...", style = MaterialTheme.typography.bodyMedium)
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                        AuthStep.WAIT_PHONE -> {
                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it },
                                label = { Text("Phone number (international)") },
                                placeholder = { Text("+4915112345678") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions {
                                    if (uiState.canRequestCode && phoneNumber.isNotBlank()) {
                                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                                            authRepository.requestCode(phoneNumber)
                                        }
                                    }
                                },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !uiState.isLoading
                            )
                            Text(
                                "You'll receive a login code via Telegram/SMS.",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        AuthStep.WAIT_CODE -> {
                            OutlinedTextField(
                                value = authCode,
                                onValueChange = { authCode = it },
                                label = { Text("Login code") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions {
                                    if (uiState.canCheckCode && authCode.isNotBlank()) {
                                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                                            authRepository.checkCode(authCode)
                                        }
                                    }
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(codeFocusRequester),
                                enabled = !uiState.isLoading
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(
                                    onClick = {
                                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                                            authRepository.resendCode()
                                        }
                                    },
                                    enabled = !uiState.isLoading
                                ) {
                                    Text("Resend code")
                                }
                            }
                        }
                        AuthStep.WAIT_PASSWORD -> {
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Two-step verification password") },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Visibility,
                                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                        )
                                    }
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions {
                                    if (uiState.canCheckPassword && password.isNotBlank()) {
                                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                                            authRepository.checkPassword(password)
                                        }
                                    }
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(passwordFocusRequester),
                                enabled = !uiState.isLoading
                            )
                            if (uiState.passwordHint?.isNotBlank() == true) {
                                Text(
                                    "Hint: ${uiState.passwordHint}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        else -> {
                            Text("Authenticating...", style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    if (uiState.errorMessage != null) {
                        Text(
                            text = uiState.errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(strokeWidth = 3.dp, modifier = Modifier.padding(end = 12.dp))
                        }
                        TextButton(
                            onClick = {
                                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                                    when (uiState.step) {
                                        AuthStep.WAIT_PHONE -> {
                                            if (phoneNumber.isNotBlank()) authRepository.requestCode(phoneNumber)
                                        }
                                        AuthStep.WAIT_CODE -> {
                                            if (authCode.isNotBlank()) authRepository.checkCode(authCode)
                                        }
                                        AuthStep.WAIT_PASSWORD -> {
                                            if (password.isNotBlank()) authRepository.checkPassword(password)
                                        }
                                        else -> {}
                                    }
                                }
                            },
                            enabled = !uiState.isLoading && when (uiState.step) {
                                AuthStep.WAIT_PHONE -> phoneNumber.isNotBlank() && phoneNumber != "+"
                                AuthStep.WAIT_CODE -> authCode.isNotBlank()
                                AuthStep.WAIT_PASSWORD -> password.isNotBlank()
                                else -> false
                            }
                        ) {
                            Text("Continue")
                        }
                    }
                }
            }

            AuthStepIndicator(current = uiState.step)
        }
    }
}

@Composable
private fun AuthStepIndicator(current: AuthStep) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AuthStepDot(active = current == AuthStep.WAIT_PHONE || current == AuthStep.INITIALIZING)
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .width(24.dp)
                .height(2.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )
        Spacer(modifier = Modifier.width(8.dp))
        AuthStepDot(active = current == AuthStep.WAIT_CODE)
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .width(24.dp)
                .height(2.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )
        Spacer(modifier = Modifier.width(8.dp))
        AuthStepDot(active = current == AuthStep.WAIT_PASSWORD || current == AuthStep.READY)
    }
}

@Composable
private fun AuthStepDot(active: Boolean) {
    val color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    Box(
        modifier = Modifier
            .width(10.dp)
            .height(10.dp)
            .clip(CircleShape)
            .background(color)
    )
}