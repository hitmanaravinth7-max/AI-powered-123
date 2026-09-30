package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomerSegmentData
import com.example.data.model.MarketingMetricEntity
import com.example.data.model.MonthlyTrend

@Composable
fun MonthlyRevenueExpenseBarChart(
    trends: List<MonthlyTrend>,
    currencySymbol: String = "$",
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Revenue & Expense Trajectory",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Monthly financial intake vs burn",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Legend
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF0284C7)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Rev", style = MaterialTheme.typography.labelSmall)
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Exp", style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (trends.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No financial records to display", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                val displayTrends = trends.takeLast(7)
                val maxVal = (displayTrends.maxOfOrNull { maxOf(it.revenue, it.expenses) } ?: 1000.0) * 1.15

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    val w = size.width
                    val h = size.height
                    val bottomMargin = 30f
                    val chartHeight = h - bottomMargin
                    val itemCount = displayTrends.size
                    val groupWidth = w / itemCount
                    val barWidth = (groupWidth * 0.32f).coerceAtMost(24f)

                    // Draw baseline
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.4f),
                        start = Offset(0f, chartHeight),
                        end = Offset(w, chartHeight),
                        strokeWidth = 1.5f
                    )

                    // Draw bars
                    displayTrends.forEachIndexed { idx, trend ->
                        val groupX = idx * groupWidth
                        val revHeight = ((trend.revenue / maxVal) * chartHeight).toFloat()
                        val expHeight = ((trend.expenses / maxVal) * chartHeight).toFloat()

                        val revX = groupX + (groupWidth / 2) - barWidth - 2f
                        val expX = groupX + (groupWidth / 2) + 2f

                        // Revenue bar
                        drawRoundRect(
                            color = Color(0xFF0284C7),
                            topLeft = Offset(revX, chartHeight - revHeight),
                            size = Size(barWidth, revHeight),
                            cornerRadius = CornerRadius(4f, 4f)
                        )

                        // Expense bar
                        drawRoundRect(
                            color = Color(0xFFF59E0B),
                            topLeft = Offset(expX, chartHeight - expHeight),
                            size = Size(barWidth, expHeight),
                            cornerRadius = CornerRadius(4f, 4f)
                        )
                    }
                }

                // Month labels below
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    displayTrends.forEach { trend ->
                        Text(
                            text = trend.monthName,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomerSegmentsDonutChart(
    segments: List<CustomerSegmentData>,
    currencySymbol: String = "$",
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Customer Segmentation",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Value & lifecycle behavioral clustering",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (segments.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No customer data available", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // Donut Chart
                    Box(
                        modifier = Modifier.size(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(130.dp)) {
                            var startAngle = -90f
                            val strokeWidth = 26f
                            val totalSpend = segments.sumOf { it.totalSpend }.coerceAtLeast(1.0)

                            segments.forEach { seg ->
                                val sweep = ((seg.totalSpend / totalSpend) * 360f).toFloat()
                                drawArc(
                                    color = Color(seg.colorHex),
                                    startAngle = startAngle,
                                    sweepAngle = sweep - 2f,
                                    useCenter = false,
                                    style = Stroke(width = strokeWidth)
                                )
                                startAngle += sweep
                            }
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${segments.sumOf { it.customerCount }}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Buyers",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Top Segments summary
                    Column(
                        modifier = Modifier.weight(1f).padding(start = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        segments.take(4).forEach { seg ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color(seg.colorHex))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = seg.segmentName,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Text(
                                    text = "${String.format("%.1f", seg.percentage)}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Recommended Segment Strategy highlight
                val topPrioritySeg = segments.firstOrNull { it.segmentName == "At-Risk" } ?: segments.firstOrNull()
                if (topPrioritySeg != null) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(topPrioritySeg.colorHex).copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "💡 Priority Action: ",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(topPrioritySeg.colorHex)
                            )
                            Text(
                                text = topPrioritySeg.recommendedAction,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChannelRoiBarChart(
    marketing: List<MarketingMetricEntity>,
    currencySymbol: String = "$",
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Marketing Channel ROI",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Acquisition spend vs revenue return",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (marketing.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No marketing channels configured", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                marketing.forEach { item ->
                    val roi = if (item.spend > 0) item.revenueGenerated / item.spend else 0.0
                    val isTopRoi = roi >= 3.0

                    Column(modifier = Modifier.padding(vertical = 6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.channel,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isTopRoi) Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFF0284C7).copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = "${String.format("%.1f", roi)}x ROI",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isTopRoi) Color(0xFF059669) else Color(0xFF0284C7)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Progress bar representing efficiency
                        LinearProgressIndicator(
                            progress = { (roi / 6.0).coerceIn(0.05, 1.0).toFloat() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (isTopRoi) Color(0xFF10B981) else Color(0xFF0284C7),
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Spend: $currencySymbol${String.format("%,.0f", item.spend)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Revenue: $currencySymbol${String.format("%,.0f", item.revenueGenerated)} (CAC: $currencySymbol${String.format("%.1f", item.cac)})",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SalesForecastCanvasChart(
    trends: List<MonthlyTrend>,
    currencySymbol: String = "$",
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Predictive Time-Series Forecast",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Regression model historical vs Q4 projection",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF6366F1).copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "87% Confidence",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF6366F1),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (trends.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Insufficient points for projection", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                val maxVal = (trends.maxOfOrNull { it.revenue } ?: 1000.0) * 1.18

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    val w = size.width
                    val h = size.height
                    val bottomMargin = 25f
                    val chartHeight = h - bottomMargin
                    val stepX = w / (trends.size - 1).coerceAtLeast(1)

                    val historicalPoints = mutableListOf<Offset>()
                    val forecastPoints = mutableListOf<Offset>()

                    trends.forEachIndexed { i, t ->
                        val x = i * stepX
                        val y = chartHeight - ((t.revenue / maxVal) * chartHeight).toFloat()
                        val pt = Offset(x, y)
                        if (t.isForecast) {
                            forecastPoints.add(pt)
                        } else {
                            historicalPoints.add(pt)
                        }
                    }

                    // Connect last historical with first forecast
                    if (historicalPoints.isNotEmpty() && forecastPoints.isNotEmpty()) {
                        forecastPoints.add(0, historicalPoints.last())
                    }

                    // Draw historical solid line
                    if (historicalPoints.size >= 2) {
                        val path = Path().apply {
                            moveTo(historicalPoints.first().x, historicalPoints.first().y)
                            for (p in historicalPoints.drop(1)) {
                                lineTo(p.x, p.y)
                            }
                        }
                        drawPath(
                            path = path,
                            color = Color(0xFF0284C7),
                            style = Stroke(width = 3.5f)
                        )
                    }

                    // Draw forecast dashed line
                    if (forecastPoints.size >= 2) {
                        val dashPath = Path().apply {
                            moveTo(forecastPoints.first().x, forecastPoints.first().y)
                            for (p in forecastPoints.drop(1)) {
                                lineTo(p.x, p.y)
                            }
                        }
                        drawPath(
                            path = dashPath,
                            color = Color(0xFF6366F1),
                            style = Stroke(
                                width = 3.5f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
                            )
                        )
                    }

                    // Draw point dots
                    historicalPoints.forEach { pt ->
                        drawCircle(color = Color(0xFF0284C7), radius = 4f, center = pt)
                        drawCircle(color = Color.White, radius = 2f, center = pt)
                    }
                    forecastPoints.drop(1).forEach { pt ->
                        drawCircle(color = Color(0xFF6366F1), radius = 4.5f, center = pt)
                        drawCircle(color = Color.White, radius = 2f, center = pt)
                    }
                }

                // Month labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    trends.forEach { t ->
                        Text(
                            text = t.monthName,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (t.isForecast) Color(0xFF6366F1) else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (t.isForecast) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}
