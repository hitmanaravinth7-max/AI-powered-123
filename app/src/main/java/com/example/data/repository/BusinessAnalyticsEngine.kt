package com.example.data.repository

import com.example.data.model.BusinessProfileEntity
import com.example.data.model.CustomerSegmentData
import com.example.data.model.ExpenseRecordEntity
import com.example.data.model.ForecastResult
import com.example.data.model.KpiSummary
import com.example.data.model.MarketingMetricEntity
import com.example.data.model.MonthlyTrend
import com.example.data.model.ProductPerformance
import com.example.data.model.RiskLevel
import com.example.data.model.SalesRecordEntity
import kotlin.math.roundToInt

object BusinessAnalyticsEngine {

    private val MONTH_NAMES = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    fun calculateKpiSummary(
        profile: BusinessProfileEntity?,
        sales: List<SalesRecordEntity>,
        expenses: List<ExpenseRecordEntity>,
        marketing: List<MarketingMetricEntity>
    ): KpiSummary {
        val totalRevenue = sales.sumOf { it.amount }
        val totalExpenses = expenses.sumOf { it.amount }
        val netProfit = totalRevenue - totalExpenses
        val profitMarginPct = if (totalRevenue > 0) (netProfit / totalRevenue) * 100 else 0.0

        val totalTransactions = sales.size
        val avgOrderValue = if (totalTransactions > 0) totalRevenue / totalTransactions else 0.0

        // Month-over-month growth: Compare last month with prior month
        val salesByMonth = sales.groupBy { it.monthIndex }
        val maxMonth = salesByMonth.keys.maxOrNull() ?: 1
        val lastMonthRevenue = salesByMonth[maxMonth]?.sumOf { it.amount } ?: 0.0
        val priorMonthRevenue = salesByMonth[maxMonth - 1]?.sumOf { it.amount } ?: (lastMonthRevenue * 0.92)

        val growthPct = if (priorMonthRevenue > 0) {
            ((lastMonthRevenue - priorMonthRevenue) / priorMonthRevenue) * 100
        } else 0.0

        val totalMarketingSpend = marketing.sumOf { it.spend }
        val marketingRevenue = marketing.sumOf { it.revenueGenerated }
        val marketingRoi = if (totalMarketingSpend > 0) marketingRevenue / totalMarketingSpend else 0.0

        // Risk Analysis heuristics
        var riskScore = 15
        val riskFactors = mutableListOf<String>()

        if (profitMarginPct < 5.0) {
            riskScore += 35
            riskFactors.add("Critical low profit margin (<5%); sensitive to slight revenue dips.")
        } else if (profitMarginPct < 15.0) {
            riskScore += 18
            riskFactors.add("Sub-optimal profit margin (<15%); operational costs compress returns.")
        }

        if (growthPct < 0) {
            riskScore += 25
            riskFactors.add("Negative month-over-month sales velocity (${String.format("%.1f", growthPct)}%).")
        }

        if (totalExpenses > totalRevenue) {
            riskScore += 40
            riskFactors.add("Cash burn alert: Operating expenses currently exceed gross revenues.")
        }

        if (marketingRoi < 2.0 && totalMarketingSpend > 1000) {
            riskScore += 15
            riskFactors.add("Marketing acquisition efficiency is below 2.0x target threshold.")
        }

        if (profile != null && lastMonthRevenue < (profile.monthlyRevenueTarget * 0.7)) {
            riskScore += 12
            riskFactors.add("Current run-rate is tracking 30%+ below your target goal.")
        }

        val riskLevel = when {
            riskScore >= 60 -> RiskLevel.HIGH
            riskScore >= 35 -> RiskLevel.MODERATE
            else -> RiskLevel.LOW
        }

        return KpiSummary(
            totalRevenue = totalRevenue,
            totalExpenses = totalExpenses,
            netProfit = netProfit,
            profitMarginPct = profitMarginPct,
            revenueGrowthPct = growthPct,
            avgOrderValue = avgOrderValue,
            totalTransactions = totalTransactions,
            totalMarketingSpend = totalMarketingSpend,
            marketingRoi = marketingRoi,
            riskLevel = riskLevel,
            riskScore = riskScore.coerceIn(0, 100),
            riskFactors = riskFactors
        )
    }

    fun calculateMonthlyTrends(
        sales: List<SalesRecordEntity>,
        expenses: List<ExpenseRecordEntity>
    ): List<MonthlyTrend> {
        val salesByMonth = sales.groupBy { it.monthIndex }
        val expensesByMonth = expenses.groupBy { it.monthIndex }

        val allMonths = (salesByMonth.keys + expensesByMonth.keys).filter { it in 1..12 }.sorted()
        if (allMonths.isEmpty()) return emptyList()

        return allMonths.map { monthIdx ->
            val monthSales = salesByMonth[monthIdx]?.sumOf { it.amount } ?: 0.0
            val monthExpenses = expensesByMonth[monthIdx]?.sumOf { it.amount } ?: (monthSales * 0.65)
            val monthName = MONTH_NAMES.getOrElse(monthIdx - 1) { "M$monthIdx" }
            MonthlyTrend(
                monthName = monthName,
                monthIndex = monthIdx,
                revenue = monthSales,
                expenses = monthExpenses,
                profit = monthSales - monthExpenses
            )
        }
    }

    fun calculateForecast(historicalTrends: List<MonthlyTrend>): ForecastResult {
        if (historicalTrends.isEmpty()) {
            return ForecastResult(
                historicalTrends = emptyList(),
                projectedNextMonthRevenue = 0.0,
                projectedNextQuarterRevenue = 0.0,
                trendDirection = "Insufficient Data",
                growthRateMonthly = 0.0,
                confidenceScore = 50
            )
        }

        // Ordinary Least Squares Linear Regression: y = m*x + c
        val n = historicalTrends.size
        val xValues = (1..n).map { it.toDouble() }
        val yValues = historicalTrends.map { it.revenue }

        val xMean = xValues.average()
        val yMean = yValues.average()

        var numerator = 0.0
        var denominator = 0.0
        for (i in 0 until n) {
            numerator += (xValues[i] - xMean) * (yValues[i] - yMean)
            denominator += (xValues[i] - xMean) * (xValues[i] - xMean)
        }

        val slope = if (denominator != 0.0) numerator / denominator else 0.0
        val intercept = yMean - slope * xMean

        // Predict next 3 months (quarter)
        val nextMonthIdx = n + 1
        val projectedMonth1 = (slope * nextMonthIdx + intercept).coerceAtLeast(yValues.lastOrNull() ?: 1000.0)
        val projectedMonth2 = (slope * (nextMonthIdx + 1) + intercept).coerceAtLeast(projectedMonth1)
        val projectedMonth3 = (slope * (nextMonthIdx + 2) + intercept).coerceAtLeast(projectedMonth2)
        val projectedQuarter = projectedMonth1 + projectedMonth2 + projectedMonth3

        val growthMonthly = if (yMean > 0) (slope / yMean) * 100 else 0.0
        val trendDirection = when {
            growthMonthly > 6.0 -> "Strong Upward Expansion (+$String.format(\"%.1f\", growthMonthly)%/mo)"
            growthMonthly > 1.5 -> "Steady Growth (+$String.format(\"%.1f\", growthMonthly)%/mo)"
            growthMonthly >= -1.5 -> "Stable / Plateauing Market Position"
            else -> "Contracting Volume ($String.format(\"%.1f\", growthMonthly)%/mo)"
        }

        // Forecast trends list
        val extendedTrends = historicalTrends.toMutableList()
        val m1Name = MONTH_NAMES.getOrElse((nextMonthIdx - 1) % 12) { "M${nextMonthIdx}" }
        val m2Name = MONTH_NAMES.getOrElse((nextMonthIdx) % 12) { "M${nextMonthIdx + 1}" }
        val m3Name = MONTH_NAMES.getOrElse((nextMonthIdx + 1) % 12) { "M${nextMonthIdx + 2}" }

        extendedTrends.add(
            MonthlyTrend(
                monthName = "$m1Name*",
                monthIndex = nextMonthIdx,
                revenue = projectedMonth1,
                expenses = projectedMonth1 * 0.68,
                profit = projectedMonth1 * 0.32,
                forecastRevenue = projectedMonth1,
                isForecast = true
            )
        )
        extendedTrends.add(
            MonthlyTrend(
                monthName = "$m2Name*",
                monthIndex = nextMonthIdx + 1,
                revenue = projectedMonth2,
                expenses = projectedMonth2 * 0.68,
                profit = projectedMonth2 * 0.32,
                forecastRevenue = projectedMonth2,
                isForecast = true
            )
        )
        extendedTrends.add(
            MonthlyTrend(
                monthName = "$m3Name*",
                monthIndex = nextMonthIdx + 2,
                revenue = projectedMonth3,
                expenses = projectedMonth3 * 0.68,
                profit = projectedMonth3 * 0.32,
                forecastRevenue = projectedMonth3,
                isForecast = true
            )
        )

        return ForecastResult(
            historicalTrends = extendedTrends,
            projectedNextMonthRevenue = projectedMonth1,
            projectedNextQuarterRevenue = projectedQuarter,
            trendDirection = trendDirection,
            growthRateMonthly = growthMonthly,
            confidenceScore = 87
        )
    }

    fun calculateCustomerSegments(sales: List<SalesRecordEntity>): List<CustomerSegmentData> {
        val totalRevenue = sales.sumOf { it.amount }
        val grouped = sales.groupBy { it.customerSegment }

        val segmentDefs = listOf(
            Triple("Champions", "Highest frequency & spend; brand advocates who drive high margins.", 0xFF10B981),
            Triple("Loyal", "Consistent repeat purchases; highly responsive to new product drops.", 0xFF0284C7),
            Triple("New", "Recent first-time buyers who need onboarding to become repeats.", 0xFF8B5CF6),
            Triple("At-Risk", "Previously frequent buyers who haven't ordered in 60-90 days.", 0xFFF59E0B),
            Triple("Hibernating", "Dormant users with low engagement; prone to permanent churn.", 0xFFEF4444)
        )

        return segmentDefs.map { (name, desc, colorHex) ->
            val records = grouped[name] ?: emptyList()
            val count = records.size
            val spend = records.sumOf { it.amount }
            val pct = if (totalRevenue > 0) (spend / totalRevenue).toFloat() * 100f else 0f

            val action = when (name) {
                "Champions" -> "Reward with VIP perks, early catalog access, and referral incentives."
                "Loyal" -> "Cross-sell complementary categories and bundle accessories."
                "New" -> "Send post-purchase satisfaction check-in and 2nd order discount."
                "At-Risk" -> "Send high-urgency winback email with free shipping or $15 voucher."
                "Hibernating" -> "Run automated sunset campaign; re-engage or clean from email list."
                else -> "Standard engagement"
            }

            CustomerSegmentData(
                segmentName = name,
                customerCount = count,
                percentage = pct,
                totalSpend = spend,
                description = desc,
                recommendedAction = action,
                colorHex = colorHex
            )
        }.sortedByDescending { it.totalSpend }
    }

    fun calculateTopProducts(sales: List<SalesRecordEntity>): List<ProductPerformance> {
        val totalRevenue = sales.sumOf { it.amount }
        val grouped = sales.groupBy { it.productName }

        return grouped.map { (name, list) ->
            val revenue = list.sumOf { it.amount }
            val units = list.sumOf { it.unitsSold }
            val category = list.firstOrNull()?.category ?: "General"
            val share = if (totalRevenue > 0) (revenue / totalRevenue).toFloat() else 0f
            ProductPerformance(
                name = name,
                category = category,
                revenue = revenue,
                units = units,
                shareOfTotal = share
            )
        }.sortedByDescending { it.revenue }
    }
}
