package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BusinessProfileEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.ExpenseRecordEntity
import com.example.data.model.MarketingMetricEntity
import com.example.data.model.RecommendationEntity
import com.example.data.model.SalesRecordEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessDao {

    // User Auth
    @Query("SELECT * FROM users LIMIT 1")
    fun getUserFlow(): Flow<UserEntity?>

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("DELETE FROM users")
    suspend fun deleteUser()

    // Business Profile
    @Query("SELECT * FROM business_profile WHERE id = 1 LIMIT 1")
    fun getProfileFlow(): Flow<BusinessProfileEntity?>

    @Query("SELECT * FROM business_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfile(): BusinessProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: BusinessProfileEntity)

    // Sales Records
    @Query("SELECT * FROM sales_records ORDER BY date DESC")
    fun getAllSalesFlow(): Flow<List<SalesRecordEntity>>

    @Query("SELECT * FROM sales_records ORDER BY date DESC")
    suspend fun getAllSales(): List<SalesRecordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: SalesRecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSales(sales: List<SalesRecordEntity>)

    @Query("DELETE FROM sales_records")
    suspend fun clearSales()

    // Expenses
    @Query("SELECT * FROM expense_records ORDER BY date DESC")
    fun getAllExpensesFlow(): Flow<List<ExpenseRecordEntity>>

    @Query("SELECT * FROM expense_records ORDER BY date DESC")
    suspend fun getAllExpenses(): List<ExpenseRecordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseRecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenses(expenses: List<ExpenseRecordEntity>)

    @Query("DELETE FROM expense_records")
    suspend fun clearExpenses()

    // Marketing Metrics
    @Query("SELECT * FROM marketing_metrics")
    fun getMarketingMetricsFlow(): Flow<List<MarketingMetricEntity>>

    @Query("SELECT * FROM marketing_metrics")
    suspend fun getMarketingMetrics(): List<MarketingMetricEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarketingMetrics(metrics: List<MarketingMetricEntity>)

    @Query("DELETE FROM marketing_metrics")
    suspend fun clearMarketingMetrics()

    // Recommendations
    @Query("SELECT * FROM recommendations ORDER BY priorityScore DESC, id ASC")
    fun getAllRecommendationsFlow(): Flow<List<RecommendationEntity>>

    @Query("SELECT * FROM recommendations ORDER BY priorityScore DESC")
    suspend fun getAllRecommendations(): List<RecommendationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecommendation(recommendation: RecommendationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecommendations(recommendations: List<RecommendationEntity>)

    @Query("UPDATE recommendations SET status = :status WHERE id = :id")
    suspend fun updateRecommendationStatus(id: Long, status: String)

    @Query("DELETE FROM recommendations")
    suspend fun clearRecommendations()

    // Chat
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllChatMessagesFlow(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChat()
}
