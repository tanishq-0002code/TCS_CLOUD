package com.example.telegramcloudgallery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.example.telegramcloudgallery.security.BiometricHelper
import com.example.telegramcloudgallery.security.SecurityManager
import kotlinx.coroutines.launch

/**
 * SecuritySettingsScreen - Manage PIN and biometric unlock.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecuritySettingsScreen(
    securityManager: SecurityManager,
    biometricHelper: BiometricHelper,
    onNavigateUp: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var pinEnabled by rememberSaveable { mutableStateOf(securityManager.isLockEnabled) }
    var biometricEnabled by rememberSaveable { mutableStateOf(securityManager.isBiometricEnabled) }
    var showSetPin by rememberSaveable { mutableStateOf(false) }
    var newPin by rememberSaveable { mutableStateOf("") }
    var confirmPin by rememberSaveable { mutableStateOf("") }
    var isSettingPin by rememberSaveable { mutableStateOf(false) }

    val canUseBiometric = biometricHelper.canAuthenticate()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App Security") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("App Lock", style = MaterialTheme.typography.titleMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Enable PIN Lock")
                        Switch(
                            checked = pinEnabled,
                            onCheckedChange = { enabled ->
                                if (enabled) {
                                    if (securityManager.hasPin()) {
                                        securityManager.enableLock()
                                        pinEnabled = true
                                    } else {
                                        showSetPin = true
                                    }
                                } else {
                                    securityManager.disableLock()
                                    pinEnabled = false
                                    biometricEnabled = false
                                    securityManager.isBiometricEnabled = false
                                }
                            }
                        )
                    }
                    if (pinEnabled) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Use Biometrics")
                            Switch(
                                checked = biometricEnabled,
                                onCheckedChange = { enabled ->
                                    if (canUseBiometric) {
                                        biometricEnabled = enabled
                                        securityManager.isBiometricEnabled = enabled
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Biometric authentication not available")
                                        }
                                    }
                                },
                                enabled = canUseBiometric
                            )
                        
                        TextButton(onClick = { showSetPin = true }) {
                            Text("Change PIN")
                        }
                    }
                }
            }

            if (showSetPin) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Set PIN", style = MaterialTheme.typography.titleMedium)
                        OutlinedTextField(
                            value = newPin,
                            onValueChange = { if (it.length <= 6 && it.all { c -> c.isDigit() }) newPin = it },
                            label = { Text("Enter 4-6 digit PIN") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Next),
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = confirmPin,
                            onValueChange = { if (it.length <= 6 && it.all { c -> c.isDigit() }) confirmPin = it },
                            label = { Text("Confirm PIN") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions {
                                if (newPin == confirmPin && newPin.length >= 4 && !isSettingPin) {
                                    scope.launch {
                                        isSettingPin = true
                                        val success = securityManager.setPin(newPin)
                                        isSettingPin = false
                                        if (success) {
                                            snackbarHostState.showSnackbar("PIN set successfully")
                                            showSetPin = false
                                            pinEnabled = true
                                            newPin = ""
                                            confirmPin = ""
                                        } else {
                                            snackbarHostState.showSnackbar("Failed to set PIN")
                                        }
                                    }
                                }
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showSetPin = false; newPin = ""; confirmPin = "" }) {
                                Text("Cancel")
                            }
                            Button(
                                onClick = {
                                    scope.launch {
                                        if (newPin == confirmPin && newPin.length >= 4) {
                                            isSettingPin = true
                                            val success = securityManager.setPin(newPin)
                                            isSettingPin = false
                                            if (success) {
                                                snackbarHostState.showSnackbar("PIN set successfully")
                                                showSetPin = false
                                                pinEnabled = true
                                                securityManager.enableLock()
                                                newPin = ""
                                                confirmPin = ""
                                            } else {
                                                snackbarHostState.showSnackbar("Failed to set PIN")
                                            }
                                        } else {
                                            snackbarHostState.showSnackbar("PINs don't match or too short")
                                        }
                                    }
                                },
                                enabled = newPin == confirmPin && newPin.length >= 4 && !isSettingPin
                            ) {
                                if (isSettingPin) {
                                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                                } else {
                                    Icon(Icons.Filled.Check, contentDescription = null)
                                    Text("Set PIN")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
