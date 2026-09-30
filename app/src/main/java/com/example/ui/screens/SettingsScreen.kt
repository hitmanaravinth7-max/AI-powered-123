package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BusinessPreset
import com.example.data.model.BusinessProfileEntity
import com.example.ui.viewmodel.DashboardUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: DashboardUiState,
    onBack: () -> Unit,
    onSaveProfile: (BusinessProfileEntity) -> Unit,
    onLoadPreset: (BusinessPreset) -> Unit,
    onSetApiKey: (String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val initialProfile = state.profile

    var bizName by remember { mutableStateOf(initialProfile?.businessName ?: "UrbanStyle Apparel") }
    var industry by remember { mutableStateOf(initialProfile?.industry ?: "E-Commerce & Retail") }
    var targetRevText by remember { mutableStateOf(initialProfile?.monthlyRevenueTarget?.toString() ?: "85000.0") }
    var budgetText by remember { mutableStateOf(initialProfile?.monthlyBudget?.toString() ?: "52000.0") }
    var growthGoal by remember { mutableStateOf(initialProfile?.growthGoal ?: "Scale Revenue to $100k/mo & Boost Customer Lifetime Value") }
    var audience by remember { mutableStateOf(initialProfile?.targetAudience ?: "Urban professionals aged 24-42") }
    var teamSize by remember { mutableStateOf(initialProfile?.teamSize ?: "6-15 (Growing team)") }
    var primaryChannel by remember { mutableStateOf(initialProfile?.primaryChannel ?: "Meta Ads & Direct Web Storefront") }
    var apiKeyText by remember { mutableStateOf(state.customApiKey) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Business Profile & Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button_settings")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // Business Profile Setup Form
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Business, contentDescription = null, tint = Color(0xFF0284C7))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Company Parameters", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }

                        OutlinedTextField(
                            value = bizName,
                            onValueChange = { bizName = it },
                            label = { Text("Company Name") },
                            modifier = Modifier.fillMaxWidth().testTag("settings_biz_name_input")
                        )

                        OutlinedTextField(
                            value = industry,
                            onValueChange = { industry = it },
                            label = { Text("Industry / Sector") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = targetRevText,
                                onValueChange = { targetRevText = it },
                                label = { Text("Monthly Target ($)") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = budgetText,
                                onValueChange = { budgetText = it },
                                label = { Text("Monthly Budget ($)") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        OutlinedTextField(
                            value = growthGoal,
                            onValueChange = { growthGoal = it },
                            label = { Text("Primary Growth Objective") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = audience,
                            onValueChange = { audience = it },
                            label = { Text("Target Customer Profile") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = primaryChannel,
                            onValueChange = { primaryChannel = it },
                            label = { Text("Main Sales & Acquisition Channel") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                val updated = BusinessProfileEntity(
                                    id = 1L,
                                    businessName = bizName,
                                    industry = industry,
                                    monthlyRevenueTarget = targetRevText.toDoubleOrNull() ?: 80000.0,
                                    monthlyBudget = budgetText.toDoubleOrNull() ?: 50000.0,
                                    growthGoal = growthGoal,
                                    targetAudience = audience,
                                    teamSize = teamSize,
                                    primaryChannel = primaryChannel,
                                    currencySymbol = "$"
                                )
                                onSaveProfile(updated)
                            },
                            modifier = Modifier.fillMaxWidth().testTag("save_profile_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save Profile Changes", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Gemini API Key Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Key, contentDescription = null, tint = Color(0xFF6366F1))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Gemini API Integration", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }

                        Text(
                            text = "AI Studio automatically binds GEMINI_API_KEY from the Secrets panel. You can also paste an optional custom key below.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        OutlinedTextField(
                            value = apiKeyText,
                            onValueChange = {
                                apiKeyText = it
                                onSetApiKey(it)
                            },
                            label = { Text("Custom Gemini API Key (Optional)") },
                            placeholder = { Text("AIzaSy...") },
                            modifier = Modifier.fillMaxWidth().testTag("custom_api_key_input")
                        )
                    }
                }
            }

            // Quick Switch Presets Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Refresh, contentDescription = null, tint = Color(0xFF10B981))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Demo Business Archetypes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }

                        Text(
                            text = "Switch between realistic industry datasets to test insights:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        BusinessPreset.values().forEach { preset ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onLoadPreset(preset) },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(preset.title, fontWeight = FontWeight.Bold)
                                        Text(preset.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFF0284C7).copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = "Load",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFF0284C7),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Sign out
            item {
                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth().testTag("logout_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444))
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign Out of Consultant Portal")
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
