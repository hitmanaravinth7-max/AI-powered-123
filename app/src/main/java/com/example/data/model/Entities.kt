package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val email: String,
    val role: String,
    val isLoggedIn: Boolean = true
)

@Entity(tableName = "business_profile")
data class BusinessProfileEntity(
    @PrimaryKey val id: Long = 1L,
    val businessName: String,
    val industry: String,
    val monthlyRevenueTarget: Double,
    val monthlyBudget: Double,
    val growthGoal: String,
    val targetAudience: String,
    val teamSize: String,
    val primaryChannel: String,
    val currencySymbol: String = "$"
)

@Entity(tableName = "sales_records")
data class SalesRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val monthIndex: Int,
    val productName: String,
    val category: String,
    val channel: String,
    val amount: Double,
    val customerSegment: String,
    val unitsSold: Int = 1
)

@Entity(tableName = "expense_records")
data class ExpenseRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val monthIndex: Int,
    val category: String,
    val amount: Double,
    val description: String,
    val isRecurring: Boolean = true
)

@Entity(tableName = "marketing_metrics")
data class MarketingMetricEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val channel: String,
    val spend: Double,
    val conversions: Int,
    val revenueGenerated: Double,
    val cac: Double
)

@Entity(tableName = "recommendations")
data class RecommendationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // REVENUE, COST_REDUCTION, MARKETING, RISK_MITIGATION
    val impact: String, // HIGH, MEDIUM, LOW
    val effort: String, // LOW, MEDIUM, HIGH
    val estimatedImpact: String,
    val description: String,
    val actionSteps: String,
    val status: String = "PENDING", // PENDING, IN_PROGRESS, COMPLETED, DISMISSED
    val priorityScore: Int = 5
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // USER, ASSISTANT
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)
