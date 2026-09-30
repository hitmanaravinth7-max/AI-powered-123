package com.example.data.repository

import com.example.data.db.BusinessDao
import com.example.data.model.BusinessPreset
import com.example.data.model.BusinessProfileEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.CustomerSegmentData
import com.example.data.model.ExpenseRecordEntity
import com.example.data.model.ForecastResult
import com.example.data.model.KpiSummary
import com.example.data.model.MarketingMetricEntity
import com.example.data.model.MonthlyTrend
import com.example.data.model.ProductPerformance
import com.example.data.model.RecommendationEntity
import com.example.data.model.SalesRecordEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class BusinessRepository(
    private val dao: BusinessDao,
    private val aiService: GeminiConsultantService = GeminiConsultantService()
) {

    val userFlow: Flow<UserEntity?> = dao.getUserFlow()
    val profileFlow: Flow<BusinessProfileEntity?> = dao.getProfileFlow()
    val salesFlow: Flow<List<SalesRecordEntity>> = dao.getAllSalesFlow()
    val expensesFlow: Flow<List<ExpenseRecordEntity>> = dao.getAllExpensesFlow()
    val marketingFlow: Flow<List<MarketingMetricEntity>> = dao.getMarketingMetricsFlow()
    val recommendationsFlow: Flow<List<RecommendationEntity>> = dao.getAllRecommendationsFlow()
    val chatFlow: Flow<List<ChatMessageEntity>> = dao.getAllChatMessagesFlow()

    suspend fun loginUser(name: String, email: String, role: String) {
        val user = UserEntity(name = name.ifBlank { "Alex Mercer" }, email = email.ifBlank { "alex@business.com" }, role = role.ifBlank { "Founder & Managing Director" })
        dao.insertUser(user)
    }

    suspend fun logout() {
        dao.deleteUser()
    }

    suspend fun saveProfile(profile: BusinessProfileEntity) {
        dao.insertProfile(profile)
    }

    suspend fun loadPreset(preset: BusinessPreset) {
        val bundle = SampleDataGenerator.generatePreset(preset)
        dao.insertProfile(bundle.profile)
        dao.clearSales()
        dao.insertSales(bundle.sales)
        dao.clearExpenses()
        dao.insertExpenses(bundle.expenses)
        dao.clearMarketingMetrics()
        dao.insertMarketingMetrics(bundle.marketing)
        dao.clearRecommendations()
        dao.insertRecommendations(bundle.recommendations)
    }

    suspend fun addSalesRecord(
        productName: String,
        category: String,
        amount: Double,
        channel: String,
        customerSegment: String,
        unitsSold: Int
    ) {
        val currentMonth = 9
        val record = SalesRecordEntity(
            date = "2026-09-29",
            monthIndex = currentMonth,
            productName = productName,
            category = category,
            channel = channel,
            amount = amount,
            customerSegment = customerSegment,
            unitsSold = unitsSold
        )
        dao.insertSale(record)
    }

    suspend fun addExpenseRecord(
        category: String,
        amount: Double,
        description: String,
        isRecurring: Boolean
    ) {
        val record = ExpenseRecordEntity(
            date = "2026-09-29",
            monthIndex = 9,
            category = category,
            amount = amount,
            description = description,
            isRecurring = isRecurring
        )
        dao.insertExpense(record)
    }

    suspend fun parseAndImportCsv(csvText: String): Int {
        val lines = csvText.lines().map { it.trim() }.filter { it.isNotEmpty() }
        var count = 0
        val salesList = mutableListOf<SalesRecordEntity>()

        for ((idx, line) in lines.withIndex()) {
            if (idx == 0 && line.lowercase().contains("product") || line.lowercase().contains("date")) {
                // Header row
                continue
            }
            val parts = line.split(",").map { it.trim().removeSurrounding("\"") }
            if (parts.size >= 3) {
                try {
                    val prod = parts[0]
                    val amount = parts.getOrNull(1)?.toDoubleOrNull() ?: 100.0
                    val category = parts.getOrNull(2) ?: "General"
                    val channel = parts.getOrNull(3) ?: "Direct"
                    val segment = parts.getOrNull(4) ?: "Loyal"
                    val units = parts.getOrNull(5)?.toIntOrNull() ?: 1

                    salesList.add(
                        SalesRecordEntity(
                            date = "2026-09-29",
                            monthIndex = 9,
                            productName = prod,
                            category = category,
                            channel = channel,
                            amount = amount,
                            customerSegment = segment,
                            unitsSold = units
                        )
                    )
                    count++
                } catch (_: Exception) {}
            }
        }

        if (salesList.isNotEmpty()) {
            dao.insertSales(salesList)
        }
        return count
    }

    suspend fun updateRecommendationStatus(id: Long, status: String) {
        dao.updateRecommendationStatus(id, status)
    }

    suspend fun sendChatMessage(
        userMessage: String,
        profile: BusinessProfileEntity?,
        kpiSummary: KpiSummary,
        topProducts: List<ProductPerformance>,
        customerSegments: List<CustomerSegmentData>,
        marketing: List<MarketingMetricEntity>,
        customApiKey: String?
    ) {
        // Save user message
        dao.insertChatMessage(
            ChatMessageEntity(
                sender = "USER",
                message = userMessage
            )
        )

        // Generate response from AI
        val aiReply = aiService.consult(
            userQuery = userMessage,
            profile = profile,
            kpiSummary = kpiSummary,
            topProducts = topProducts,
            customerSegments = customerSegments,
            marketing = marketing,
            customApiKey = customApiKey
        )

        // Save AI reply
        dao.insertChatMessage(
            ChatMessageEntity(
                sender = "ASSISTANT",
                message = aiReply
            )
        )
    }

    suspend fun clearChat() {
        dao.clearChat()
    }
}
