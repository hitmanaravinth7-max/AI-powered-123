package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
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
import com.example.data.repository.BusinessAnalyticsEngine
import com.example.data.repository.BusinessRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DashboardUiState(
    val user: UserEntity? = null,
    val profile: BusinessProfileEntity? = null,
    val kpiSummary: KpiSummary = KpiSummary(),
    val monthlyTrends: List<MonthlyTrend> = emptyList(),
    val forecast: ForecastResult = ForecastResult(emptyList(), 0.0, 0.0, "", 0.0, 0),
    val customerSegments: List<CustomerSegmentData> = emptyList(),
    val topProducts: List<ProductPerformance> = emptyList(),
    val marketingMetrics: List<MarketingMetricEntity> = emptyList(),
    val recommendations: List<RecommendationEntity> = emptyList(),
    val chatMessages: List<ChatMessageEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isAiThinking: Boolean = false,
    val selectedTimeFilter: String = "Year-to-Date",
    val selectedRecCategory: String = "ALL",
    val customApiKey: String = ""
)

class BusinessViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BusinessRepository

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking = _isAiThinking.asStateFlow()

    private val _selectedTimeFilter = MutableStateFlow("Year-to-Date")
    val selectedTimeFilter = _selectedTimeFilter.asStateFlow()

    private val _selectedRecCategory = MutableStateFlow("ALL")
    val selectedRecCategory = _selectedRecCategory.asStateFlow()

    private val _customApiKey = MutableStateFlow("")
    val customApiKey = _customApiKey.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage = _toastMessage.asStateFlow()

    init {
        val db = AppDatabase.getInstance(application)
        repository = BusinessRepository(db.businessDao())

        // Check if DB is initialized with initial default data
        viewModelScope.launch {
            repository.profileFlow.collect { profile ->
                if (profile == null) {
                    // Preload with default realistic E-commerce scenario
                    repository.loadPreset(BusinessPreset.ECOMMERCE)
                }
            }
        }
    }

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.userFlow,
        repository.profileFlow,
        repository.salesFlow,
        repository.expensesFlow,
        repository.marketingFlow,
        repository.recommendationsFlow,
        repository.chatFlow,
        _isAiThinking,
        _selectedTimeFilter,
        _selectedRecCategory,
        _customApiKey
    ) { args ->
        val user = args[0] as? UserEntity
        val profile = args[1] as? BusinessProfileEntity
        val sales = (args[2] as? List<*>)?.filterIsInstance<SalesRecordEntity>() ?: emptyList()
        val expenses = (args[3] as? List<*>)?.filterIsInstance<ExpenseRecordEntity>() ?: emptyList()
        val marketing = (args[4] as? List<*>)?.filterIsInstance<MarketingMetricEntity>() ?: emptyList()
        val recommendations = (args[5] as? List<*>)?.filterIsInstance<RecommendationEntity>() ?: emptyList()
        val chatMessages = (args[6] as? List<*>)?.filterIsInstance<ChatMessageEntity>() ?: emptyList()
        val isThinking = args[7] as Boolean
        val timeFilter = args[8] as String
        val recCategory = args[9] as String
        val apiKey = args[10] as String

        // Compute analytical indicators
        val kpi = BusinessAnalyticsEngine.calculateKpiSummary(profile, sales, expenses, marketing)
        val trends = BusinessAnalyticsEngine.calculateMonthlyTrends(sales, expenses)
        val forecast = BusinessAnalyticsEngine.calculateForecast(trends)
        val segments = BusinessAnalyticsEngine.calculateCustomerSegments(sales)
        val topProds = BusinessAnalyticsEngine.calculateTopProducts(sales)

        val filteredRecs = if (recCategory == "ALL") {
            recommendations
        } else {
            recommendations.filter { it.category == recCategory }
        }

        DashboardUiState(
            user = user,
            profile = profile,
            kpiSummary = kpi,
            monthlyTrends = trends,
            forecast = forecast,
            customerSegments = segments,
            topProducts = topProds,
            marketingMetrics = marketing,
            recommendations = filteredRecs,
            chatMessages = chatMessages,
            isAiThinking = isThinking,
            selectedTimeFilter = timeFilter,
            selectedRecCategory = recCategory,
            customApiKey = apiKey
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState(isLoading = true)
    )

    fun login(name: String, email: String, role: String) {
        viewModelScope.launch {
            repository.loginUser(name, email, role)
            _toastMessage.value = "Welcome back, $name"
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _toastMessage.value = "Signed out"
        }
    }

    fun updateProfile(profile: BusinessProfileEntity) {
        viewModelScope.launch {
            repository.saveProfile(profile)
            _toastMessage.value = "Business profile saved"
        }
    }

    fun loadPreset(preset: BusinessPreset) {
        viewModelScope.launch {
            repository.loadPreset(preset)
            _toastMessage.value = "Loaded ${preset.title} dataset"
        }
    }

    fun addSale(
        product: String,
        category: String,
        amount: Double,
        channel: String,
        segment: String,
        units: Int
    ) {
        viewModelScope.launch {
            repository.addSalesRecord(product, category, amount, channel, segment, units)
            _toastMessage.value = "Added sale: $product ($${String.format("%.2f", amount)})"
        }
    }

    fun addExpense(category: String, amount: Double, description: String, isRecurring: Boolean) {
        viewModelScope.launch {
            repository.addExpenseRecord(category, amount, description, isRecurring)
            _toastMessage.value = "Logged expense: $description"
        }
    }

    fun importCsv(csvText: String) {
        viewModelScope.launch {
            val count = repository.parseAndImportCsv(csvText)
            _toastMessage.value = "Imported $count transaction records successfully"
        }
    }

    fun toggleRecommendationStatus(id: Long, currentStatus: String) {
        viewModelScope.launch {
            val nextStatus = when (currentStatus) {
                "PENDING" -> "IN_PROGRESS"
                "IN_PROGRESS" -> "COMPLETED"
                "COMPLETED" -> "PENDING"
                else -> "PENDING"
            }
            repository.updateRecommendationStatus(id, nextStatus)
        }
    }

    fun sendChatMessage(message: String) {
        if (message.isBlank()) return
        val state = uiState.value
        viewModelScope.launch {
            _isAiThinking.value = true
            try {
                repository.sendChatMessage(
                    userMessage = message,
                    profile = state.profile,
                    kpiSummary = state.kpiSummary,
                    topProducts = state.topProducts,
                    customerSegments = state.customerSegments,
                    marketing = state.marketingMetrics,
                    customApiKey = _customApiKey.value.ifBlank { null }
                )
            } finally {
                _isAiThinking.value = false
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChat()
            _toastMessage.value = "Consultation history cleared"
        }
    }

    fun setTimeFilter(filter: String) {
        _selectedTimeFilter.value = filter
    }

    fun setRecCategory(category: String) {
        _selectedRecCategory.value = category
    }

    fun setCustomApiKey(key: String) {
        _customApiKey.value = key
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
