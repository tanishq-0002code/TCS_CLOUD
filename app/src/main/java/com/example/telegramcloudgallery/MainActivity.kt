package com.example.telegramcloudgallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.telegramcloudgallery.data.telegram.TelegramClientManager
import com.example.telegramcloudgallery.ui.screens.AuthScreen
import com.example.telegramcloudgallery.ui.screens.FolderManagementScreen
import com.example.telegramcloudgallery.ui.screens.GalleryScreen
import com.example.telegramcloudgallery.ui.screens.LockScreen
import com.example.telegramcloudgallery.ui.screens.ReelFeedScreen
import com.example.telegramcloudgallery.ui.screens.SecuritySettingsScreen
import com.example.telegramcloudgallery.ui.screens.SyncSettingsScreen
import com.example.telegramcloudgallery.ui.theme.TelegramCloudGalleryTheme

/**
 * Main activity with navigation and permissions.
 */
class MainActivity : ComponentActivity() {
    private lateinit var appContainer: AppContainer

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Permissions result handled - app continues regardless
    }

    private fun requestRequiredPermissions() {
        val permissions = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            permissions.add(Manifest.permission.READ_MEDIA_VIDEO)
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        requestRequiredPermissions()

        appContainer = (application as TelegramGalleryApp).appContainer
        TelegramClientManager.initialize()

        setContent {
            TelegramCloudGalleryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "gallery") {
                        composable("gallery") {
                            GalleryScreen(
                                database = appContainer.database,
                                onNavigateToFolders = { navController.navigate("folders") },
                                onNavigateToSyncSettings = { navController.navigate("sync") },
                                onNavigateToReels = { navController.navigate("reels") },
                                onNavigateToAuth = { navController.navigate("auth") },
                                onNavigateToSecurity = { navController.navigate("security") }
                            )
                        }
                        composable("auth") {
                            AuthScreen(
                                authRepository = appContainer.authRepository,
                                onAuthSuccess = {
                                    navController.navigate("gallery") {
                                        popUpTo("auth") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("reels") {
                            ReelFeedScreen(
                                database = appContainer.database,
                                onNavigateUp = { navController.popBackStack() }
                            )
                        }
                        composable("folders") {
                            FolderManagementScreen(
                                database = appContainer.database,
                                onNavigateUp = { navController.popBackStack() }
                            )
                        }
                        composable("sync") {
                            SyncSettingsScreen(
                                syncPreferences = appContainer.syncPreferences,
                                syncManager = appContainer.syncManager,
                                onNavigateUp = { navController.popBackStack() }
                            )
                        }
                        composable("security") {
                            SecuritySettingsScreen(
                                securityManager = appContainer.securityManager,
                                biometricHelper = appContainer.biometricHelper,
                                onNavigateUp = { navController.popBackStack() }
                            )
                        }
                        composable("lock") {
                            LockScreen(
                                securityManager = appContainer.securityManager,
                                biometricHelper = appContainer.biometricHelper,
                                onUnlockSuccess = {
                                    navController.navigate("gallery") {
                                        popUpTo("lock") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}