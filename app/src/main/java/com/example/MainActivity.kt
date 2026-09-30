package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.AiChatScreen
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.RecommendationsScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.BusinessViewModel

enum class AppScreen {
    AUTH,
    DASHBOARD,
    ANALYTICS,
    RECOMMENDATIONS,
    AI_CHAT,
    REPORTS,
    SETTINGS
}

data class BottomNavItem(
    val screen: AppScreen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppRoot()
            }
        }
    }
}

@Composable
fun MainAppRoot(
    viewModel: BusinessViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Navigation screen state
    var currentScreen by remember { mutableStateOf(AppScreen.DASHBOARD) }
    var chatInitialQuery by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    val bottomNavItems = listOf(
        BottomNavItem(AppScreen.DASHBOARD, "Dashboard", Icons.Filled.Dashboard, Icons.Outlined.Dashboard),
        BottomNavItem(AppScreen.ANALYTICS, "Analytics", Icons.Filled.BarChart, Icons.Outlined.BarChart),
        BottomNavItem(AppScreen.RECOMMENDATIONS, "Actions", Icons.Filled.Checklist, Icons.Outlined.Checklist),
        BottomNavItem(AppScreen.AI_CHAT, "AI Advisor", Icons.AutoMirrored.Filled.Chat, Icons.AutoMirrored.Outlined.Chat),
        BottomNavItem(AppScreen.REPORTS, "Reports", Icons.Filled.Description, Icons.Outlined.Description)
    )

    // Hardware back navigation handling
    if (currentScreen != AppScreen.DASHBOARD && currentScreen != AppScreen.AUTH) {
        BackHandler {
            currentScreen = AppScreen.DASHBOARD
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (currentScreen != AppScreen.AUTH) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentScreen == item.screen
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (item.screen == AppScreen.AI_CHAT) {
                                    chatInitialQuery = null
                                }
                                currentScreen = item.screen
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF0284C7),
                                selectedTextColor = Color(0xFF0284C7),
                                indicatorColor = Color(0xFF0284C7).copy(alpha = 0.12f)
                            ),
                            modifier = Modifier.testTag("nav_${item.label.lowercase().replace(" ", "_")}")
                        )
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.AUTH -> {
                    AuthScreen(
                        onLoginSuccess = { name, email, role ->
                            viewModel.login(name, email, role)
                            currentScreen = AppScreen.DASHBOARD
                        }
                    )
                }

                AppScreen.DASHBOARD -> {
                    DashboardScreen(
                        state = uiState,
                        onNavigateToChat = {
                            chatInitialQuery = null
                            currentScreen = AppScreen.AI_CHAT
                        },
                        onNavigateToAnalytics = {
                            currentScreen = AppScreen.ANALYTICS
                        },
                        onNavigateToRecommendations = {
                            currentScreen = AppScreen.RECOMMENDATIONS
                        },
                        onNavigateToSettings = {
                            currentScreen = AppScreen.SETTINGS
                        },
                        onTimeFilterSelected = { filter ->
                            viewModel.setTimeFilter(filter)
                        },
                        onToggleRecStatus = { id, currentStatus ->
                            viewModel.toggleRecommendationStatus(id, currentStatus)
                        },
                        onQuickAddSale = {
                            currentScreen = AppScreen.ANALYTICS
                        }
                    )
                }

                AppScreen.ANALYTICS -> {
                    AnalyticsScreen(
                        state = uiState,
                        onBack = { currentScreen = AppScreen.DASHBOARD },
                        onAddSale = { prod, cat, amt, ch, seg, units ->
                            viewModel.addSale(prod, cat, amt, ch, seg, units)
                        },
                        onAddExpense = { cat, amt, desc, recurring ->
                            viewModel.addExpense(cat, amt, desc, recurring)
                        },
                        onImportCsv = { csv ->
                            viewModel.importCsv(csv)
                        },
                        onLoadPreset = { preset ->
                            viewModel.loadPreset(preset)
                        }
                    )
                }

                AppScreen.RECOMMENDATIONS -> {
                    RecommendationsScreen(
                        state = uiState,
                        onBack = { currentScreen = AppScreen.DASHBOARD },
                        onCategoryFilterSelected = { category ->
                            viewModel.setRecCategory(category)
                        },
                        onToggleRecStatus = { id, status ->
                            viewModel.toggleRecommendationStatus(id, status)
                        },
                        onAskAiAboutRec = { title ->
                            chatInitialQuery = "How can our team execute this recommendation effectively: '$title'?"
                            currentScreen = AppScreen.AI_CHAT
                        }
                    )
                }

                AppScreen.AI_CHAT -> {
                    AiChatScreen(
                        state = uiState,
                        initialQuery = chatInitialQuery,
                        onBack = { currentScreen = AppScreen.DASHBOARD },
                        onSendMessage = { query ->
                            viewModel.sendChatMessage(query)
                        },
                        onClearChat = {
                            viewModel.clearChat()
                        }
                    )
                }

                AppScreen.REPORTS -> {
                    ReportsScreen(
                        state = uiState,
                        onBack = { currentScreen = AppScreen.DASHBOARD }
                    )
                }

                AppScreen.SETTINGS -> {
                    SettingsScreen(
                        state = uiState,
                        onBack = { currentScreen = AppScreen.DASHBOARD },
                        onSaveProfile = { profile ->
                            viewModel.updateProfile(profile)
                        },
                        onLoadPreset = { preset ->
                            viewModel.loadPreset(preset)
                        },
                        onSetApiKey = { key ->
                            viewModel.setCustomApiKey(key)
                        },
                        onLogout = {
                            viewModel.logout()
                            currentScreen = AppScreen.AUTH
                        }
                    )
                }
            }
        }
    }
}
