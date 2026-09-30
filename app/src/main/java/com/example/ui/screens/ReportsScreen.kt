package com.example.ui.screens

import android.content.Context
import android.content.Intent
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.DashboardUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    state: DashboardUiState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val curr = state.profile?.currencySymbol ?: "$"
    val kpi = state.kpiSummary
    val profile = state.profile

    val reportText = buildExecutiveReport(state)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Executive Business Report", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button_reports")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            shareReport(context, reportText)
                        },
                        modifier = Modifier.testTag("share_report_button")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share Report")
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
                // Action Header
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = Color(0xFF0284C7),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Ready-to-Export Board Summary",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Download, print, or share directly to your executive team",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { shareReport(context, reportText) },
                                modifier = Modifier.weight(1f).testTag("action_share_report"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Export / Share")
                            }

                            OutlinedButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(reportText))
                                },
                                modifier = Modifier.weight(1f).testTag("action_copy_report"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Copy Text")
                            }
                        }
                    }
                }
            }

            // Preview Document Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = reportText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}

private fun buildExecutiveReport(state: DashboardUiState): String {
    val curr = state.profile?.currencySymbol ?: "$"
    val kpi = state.kpiSummary
    val profile = state.profile
    val forecast = state.forecast

    return """=================================================
       EXECUTIVE BUSINESS CONSULTING REPORT
            Generated by BizConsult AI
=================================================
COMPANY:     ${profile?.businessName ?: "UrbanStyle Apparel"}
INDUSTRY:    ${profile?.industry ?: "E-Commerce"}
PERIOD:      Year-to-Date (2026)
DATE:        September 29, 2026
TARGET GOAL: ${profile?.growthGoal ?: "Scale Revenue"}

-------------------------------------------------
1. KEY FINANCIAL PERFORMANCE METRICS
-------------------------------------------------
• Gross Revenue:       $curr${String.format("%,.0f", kpi.totalRevenue)}
• Total Expenses:      $curr${String.format("%,.0f", kpi.totalExpenses)}
• Net Profit:          $curr${String.format("%,.0f", kpi.netProfit)}
• Net Profit Margin:   ${String.format("%.1f", kpi.profitMarginPct)}%
• MoM Sales Velocity:  ${if (kpi.revenueGrowthPct >= 0) "+" else ""}${String.format("%.1f", kpi.revenueGrowthPct)}%
• Average Order Value: $curr${String.format("%.2f", kpi.avgOrderValue)}
• Total Transactions:  ${kpi.totalTransactions}

-------------------------------------------------
2. RISK & SUSTAINABILITY INDEX
-------------------------------------------------
• Overall Risk Status: ${kpi.riskLevel.label}
• Calculated Score:    ${kpi.riskScore} / 100
• Identified Factors:
${if (kpi.riskFactors.isNotEmpty()) kpi.riskFactors.joinToString("\n") { "  - $it" } else "  - Baseline financial health is stable."}

-------------------------------------------------
3. PREDICTIVE SALES FORECAST (Q4 OUTLOOK)
-------------------------------------------------
• Trend Velocity:     ${forecast.trendDirection}
• Projected Month +1: $curr${String.format("%,.0f", forecast.projectedNextMonthRevenue)}
• Projected Quarter:  $curr${String.format("%,.0f", forecast.projectedNextQuarterRevenue)}
• Model Confidence:   ${forecast.confidenceScore}%

-------------------------------------------------
4. MARKETING CAPITAL EFFICIENCY
-------------------------------------------------
• Total Ad Capital:   $curr${String.format("%,.0f", kpi.totalMarketingSpend)}
• Portfolio ROI:      ${String.format("%.2f", kpi.marketingRoi)}x
${state.marketingMetrics.joinToString("\n") { "• ${it.channel}: Spend $curr${String.format("%,.0f", it.spend)} -> Rev $curr${String.format("%,.0f", it.revenueGenerated)} (CAC $curr${String.format("%.1f", it.cac)})" }}

-------------------------------------------------
5. PRIORITIZED STRATEGIC RECOMMENDATIONS
-------------------------------------------------
${state.recommendations.take(4).mapIndexed { idx, rec -> 
"""[Action #${idx + 1}] ${rec.title}
Category:   ${rec.category} | Impact: ${rec.impact} | Effort: ${rec.effort}
Projected:  ${rec.estimatedImpact}
Rationale:  ${rec.description}
Status:     ${rec.status}"""
}.joinToString("\n\n")}

=================================================
CONFIDENTIAL & PROPRIETARY — BIZCONSULT AI
================================================="""
}

private fun shareReport(context: Context, reportText: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, reportText)
        putExtra(Intent.EXTRA_SUBJECT, "Executive Business Consultation Report")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Export Business Report")
    context.startActivity(shareIntent)
}
