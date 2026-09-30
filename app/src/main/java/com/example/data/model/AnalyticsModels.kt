package com.example.data.model

enum class RiskLevel(val label: String, val colorHex: Long) {
    LOW("Low Risk - Healthy", 0xFF10B981),
    MODERATE("Moderate Risk - Monitor", 0xFFF59E0B),
    HIGH("High Risk - Action Required", 0xFFEF4444)
}

data class KpiSummary(
    val totalRevenue: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val netProfit: Double = 0.0,
    val profitMarginPct: Double = 0.0,
    val revenueGrowthPct: Double = 0.0,
    val avgOrderValue: Double = 0.0,
    val totalTransactions: Int = 0,
    val totalMarketingSpend: Double = 0.0,
    val marketingRoi: Double = 0.0,
    val riskLevel: RiskLevel = RiskLevel.LOW,
    val riskScore: Int = 18,
    val riskFactors: List<String> = emptyList()
)

data class MonthlyTrend(
    val monthName: String,
    val monthIndex: Int,
    val revenue: Double,
    val expenses: Double,
    val profit: Double,
    val forecastRevenue: Double? = null,
    val isForecast: Boolean = false
)

data class ProductPerformance(
    val name: String,
    val category: String,
    val revenue: Double,
    val units: Int,
    val shareOfTotal: Float
)

data class CustomerSegmentData(
    val segmentName: String,
    val customerCount: Int,
    val percentage: Float,
    val totalSpend: Double,
    val description: String,
    val recommendedAction: String,
    val colorHex: Long
)

data class ForecastResult(
    val historicalTrends: List<MonthlyTrend>,
    val projectedNextMonthRevenue: Double,
    val projectedNextQuarterRevenue: Double,
    val trendDirection: String,
    val growthRateMonthly: Double,
    val confidenceScore: Int
)

enum class BusinessPreset(val title: String, val subtitle: String, val industry: String) {
    ECOMMERCE(
        title = "UrbanStyle Apparel",
        subtitle = "Direct-to-Consumer Fashion & Accessories",
        industry = "E-Commerce & Retail"
    ),
    SAAS(
        title = "CloudFlow Solutions",
        subtitle = "B2B Workflow Automation Platform",
        industry = "SaaS & Software"
    ),
    CAFE(
        title = "Artisan Roast & Bakery",
        subtitle = "Multi-location Specialty Coffee & Pastry",
        industry = "Food & Hospitality"
    ),
    CONSULTING(
        title = "Vanguard Growth Advisory",
        subtitle = "B2B Strategic Marketing & Strategy",
        industry = "Professional Services"
    )
}
