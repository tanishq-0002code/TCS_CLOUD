package com.example.telegramcloudgallery.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.telegramcloudgallery.security.BiometricHelper
import com.example.telegramcloudgallery.security.SecurityManager
import kotlinx.coroutines.launch

/**
 * LockScreen - Fully wired with SecurityManager and BiometricHelper.
 * Shows PIN pad and biometric option. Auto-triggers biometric if enabled.
 */
@Composable
fun LockScreen(
    securityManager: SecurityManager,
    biometricHelper: BiometricHelper,
    onUnlockSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var pin by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var showError by rememberSaveable { mutableStateOf<String?>(null) }

    val canUseBiometric = securityManager.isBiometricEnabled && biometricHelper.canAuthenticate()

    // Auto-trigger biometric on launch if enabled
    LaunchedEffect(canUseBiometric) {
        if (canUseBiometric && activity != null) {
            biometricHelper.showBiometricPrompt(
                activity = activity,
                title = "Unlock Telegram Cloud Gallery",
                subtitle = "Use your biometric to continue",
                negativeButtonText = "Use PIN",
                onSuccess = {
                    onUnlockSuccess()
                },
                onError = { errorCode, errString ->
                    // Don't show error if user cancelled
                    if (errorCode != BiometricPrompt.ERROR_USER_CANCELED && errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        showError = errString
                    }
                },
                onFailed = {
                    showError = "Biometric authentication failed"
                }
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.88f),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "App locked",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "App Locked",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Authenticate to continue accessing your Telegram Cloud Gallery.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                PinDisplay(pin)
                if (showError != null) {
                    Text(
                        text = showError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
                PinPad(
                    onDigit = { if (pin.length < 6 && !isLoading) pin += it },
                    onBackspace = { if (pin.isNotEmpty() && !isLoading) pin = pin.dropLast(1) },
                    onClear = { if (!isLoading) pin = "" }
                )
                Button(
                    onClick = {
                        if (pin.length >= 4) {
                            isLoading = true
                            scope.launch {
                                val isValid = securityManager.verifyPin(pin)
                                isLoading = false
                                if (isValid) {
                                    pin = ""
                                    onUnlockSuccess()
                                } else {
                                    pin = ""
                                    showError = "Invalid PIN"
                                }
                            }
                        }
                    },
                    enabled = pin.length >= 4 && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Unlock")
                    }
                }
                if (canUseBiometric) {
                    TextButton(
                        onClick = {
                            if (activity != null) {
                                biometricHelper.showBiometricPrompt(
                                    activity = activity,
                                    title = "Unlock Telegram Cloud Gallery",
                                    subtitle = "Use your biometric to continue",
                                    negativeButtonText = "Cancel",
                                    onSuccess = {
                                        onUnlockSuccess()
                                    },
                                    onError = { errorCode, errString ->
                                        if (errorCode != BiometricPrompt.ERROR_USER_CANCELED && errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                                            showError = errString
                                        }
                                    },
                                    onFailed = {
                                        showError = "Biometric authentication failed"
                                    }
                                )
                            }
                        }
                    ) {
                        Text("Use biometrics instead")
                    }
                }
            }
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun PinDisplay(pin: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(6) { index ->
            val filled = index < pin.length
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(
                        if (filled) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outlineVariant
                    )
            )
        }
    }
}

@Composable
private fun PinPad(
    onDigit: (String) -> Unit,
    onBackspace: () -> Unit,
    onClear: () -> Unit
) {
    val digits = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "C", "0", "⌫")
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        (0 until 4).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                (0 until 3).forEach { col ->
                    val idx = row * 3 + col
                    val label = digits[idx]
                    PinKey(label = label) {
                        when (label) {
                            "⌫" -> onBackspace()
                            "C" -> onClear()
                            else -> onDigit(label)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PinKey(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(56.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, style = MaterialTheme.typography.titleLarge)
    }
}