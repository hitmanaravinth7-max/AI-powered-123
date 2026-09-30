package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.RecommendationEntity
import com.example.ui.components.ChannelRoiBarChart
import com.example.ui.components.CustomerSegmentsDonutChart
import com.example.ui.components.MetricCard
import com.example.ui.components.MonthlyRevenueExpenseBarChart
import com.example.ui.components.RiskGaugeCard
import com.example.ui.viewmodel.DashboardUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onNavigateToChat: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToRecommendations: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onTimeFilterSelected: (String) -> Unit,
    onToggleRecStatus: (Long, String) -> Unit,
    onQuickAddSale: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currency = state.profile?.currencySymbol ?: "$"
    val kpi = state.kpiSummary

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0F172A)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_app_logo_1790744061345),
                                contentDescription = "Logo",
                                modifier = Modifier.size(32.dp).clip(CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = state.profile?.businessName ?: "BizConsult AI",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = state.profile?.industry ?: "Decision Support",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = onNavigateToSettings,
                        modifier = Modifier.testTag("settings_top_button")
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings & Profiles")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToChat,
                containerColor = Color(0xFF0284C7),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("ai_assistant_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = "Ask AI")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ask BizAdvisor", fontWeight = FontWeight.Bold)
                }
            }
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
                // Hero Banner
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.hero_consultant_1790744074770),
                            contentDescription = "Consultant Banner",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF0F172A).copy(alpha = 0.92f),
                                            Color(0xFF0284C7).copy(alpha = 0.65f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF10B981).copy(alpha = 0.25f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color(0xFF34D399),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "AI ADVISORY ACTIVE",
                                        color = Color(0xFF34D399),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Intelligent Decision Support",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Grounded in real-time sales, marketing & margin trends",
                                color = Color(0xFFE2E8F0),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            // Time Filter Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Executive Overview",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("30 Days", "90 Days", "Year-to-Date").forEach { filter ->
                            FilterChip(
                                selected = state.selectedTimeFilter == filter,
                                onClick = { onTimeFilterSelected(filter) },
                                label = { Text(filter, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF0284C7).copy(alpha = 0.15f),
                                    selectedLabelColor = Color(0xFF0284C7)
                                )
                            )
                        }
                    }
                }
            }

            // 2x2 Metric Cards Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Gross Revenue",
                        value = "$currency${String.format("%,.0f", kpi.totalRevenue)}",
                        deltaText = "${if (kpi.revenueGrowthPct >= 0) "+" else ""}${String.format("%.1f", kpi.revenueGrowthPct)}% MoM",
                        isPositive = kpi.revenueGrowthPct >= 0,
                        icon = Icons.AutoMirrored.Filled.TrendingUp,
                        accentColor = Color(0xFF0284C7),
                        modifier = Modifier.weight(1f),
                        testTag = "metric_gross_revenue"
                    )

                    MetricCard(
                        title = "Net Profit Margin",
                        value = "${String.format("%.1f", kpi.profitMarginPct)}%",
                        deltaText = "$currency${String.format("%,.0f", kpi.netProfit)} net",
                        isPositive = kpi.profitMarginPct >= 12.0,
                        icon = Icons.Default.MonetizationOn,
                        accentColor = Color(0xFF10B981),
                        modifier = Modifier.weight(1f),
                        testTag = "metric_net_profit"
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Avg Order Value",
                        value = "$currency${String.format("%.2f", kpi.avgOrderValue)}",
                        deltaText = "${kpi.totalTransactions} orders",
                        isPositive = true,
                        icon = Icons.Default.BarChart,
                        accentColor = Color(0xFF6366F1),
                        modifier = Modifier.weight(1f),
                        testTag = "metric_avg_order"
                    )

                    MetricCard(
                        title = "Marketing ROI",
                        value = "${String.format("%.2f", kpi.marketingRoi)}x",
                        deltaText = "$currency${String.format("%,.0f", kpi.totalMarketingSpend)} spend",
                        isPositive = kpi.marketingRoi >= 2.5,
                        icon = Icons.Default.People,
                        accentColor = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f),
                        testTag = "metric_marketing_roi"
                    )
                }
            }

            // Risk Gauge
            item {
                RiskGaugeCard(
                    riskLevel = kpi.riskLevel,
                    riskScore = kpi.riskScore,
                    riskFactors = kpi.riskFactors
                )
            }

            // Revenue & Expense Bar Chart
            item {
                MonthlyRevenueExpenseBarChart(
                    trends = state.monthlyTrends,
                    currencySymbol = currency
                )
            }

            // Prioritized AI Recommendations Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFF0284C7),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Prioritized Actions (${state.recommendations.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "View All",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF0284C7),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { onNavigateToRecommendations() }
                            .padding(4.dp)
                            .testTag("view_all_recommendations_button")
                    )
                }
            }

            // Top Recommendations Preview
            items(state.recommendations.take(3)) { rec ->
                RecommendationCardItem(
                    rec = rec,
                    onToggleStatus = { onToggleRecStatus(rec.id, rec.status) }
                )
            }

            // Customer Segmentation Donut Chart
            item {
                CustomerSegmentsDonutChart(
                    segments = state.customerSegments,
                    currencySymbol = currency
                )
            }

            // Marketing Channel ROI Breakdown
            item {
                ChannelRoiBarChart(
                    marketing = state.marketingMetrics,
                    currencySymbol = currency
                )
            }

            // Deep Dive Navigation Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToAnalytics() }
                        .testTag("open_deep_analytics_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF6366F1).copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BarChart,
                                    contentDescription = null,
                                    tint = Color(0xFF6366F1)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Deep Analytics & Forecasting",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Linear regression, data import & product shares",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = "Open")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(72.dp)) // Padding for FAB
            }
        }
    }
}

@Composable
fun RecommendationCardItem(
    rec: RecommendationEntity,
    onToggleStatus: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCompleted = rec.status == "COMPLETED"
    val isInProgress = rec.status == "IN_PROGRESS"

    val impactColor = when (rec.impact) {
        "HIGH" -> Color(0xFF10B981)
        "MEDIUM" -> Color(0xFF0284C7)
        else -> Color(0xFF64748B)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) MaterialTheme.colorScheme.surface.copy(alpha = 0.7f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = impactColor.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "${rec.impact} IMPACT",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = impactColor,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF6366F1).copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = rec.estimatedImpact,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF6366F1),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Status chip
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when {
                        isCompleted -> Color(0xFF10B981).copy(alpha = 0.15f)
                        isInProgress -> Color(0xFFF59E0B).copy(alpha = 0.15f)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    modifier = Modifier.clickable { onToggleStatus() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = when {
                                isCompleted -> "Done"
                                isInProgress -> "In Progress"
                                else -> "To Do"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                isCompleted -> Color(0xFF059669)
                                isInProgress -> Color(0xFFD97706)
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = rec.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = rec.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )
        }
    }
}
