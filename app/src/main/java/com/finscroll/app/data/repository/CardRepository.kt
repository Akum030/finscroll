package com.finscroll.app.data.repository

import com.finscroll.app.data.api.FinScrollApiService
import com.finscroll.app.data.model.CardRequest
import com.finscroll.app.data.model.CardResponse
import com.finscroll.app.data.model.FinancialCard
import com.finscroll.app.data.model.FinancialCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepository @Inject constructor(
    private val apiService: FinScrollApiService
) {
    
    /**
     * Fetch cards by topic
     */
    suspend fun fetchCards(
        topic: String,
        count: Int = 20,
        category: String? = null,
        difficulty: String? = null
    ): Result<CardResponse> = withContext(Dispatchers.IO) {
        try {
            val request = CardRequest(
                topic = topic,
                count = count,
                category = category,
                difficulty = difficulty
            )
            val response = apiService.fetchCards(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get daily mixed cards
     */
    suspend fun getDailyCards(count: Int = 20): Result<CardResponse> = withContext(Dispatchers.IO) {
        try {
            val request = CardRequest(
                topic = "Daily Financial Literacy Mix",
                count = count
            )
            val response = apiService.getDailyCards(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get cards by category
     */
    suspend fun getCardsByCategory(
        category: FinancialCategory,
        count: Int = 20
    ): Result<CardResponse> = withContext(Dispatchers.IO) {
        try {
            val request = CardRequest(
                topic = category.displayName,
                count = count,
                category = category.name.lowercase()
            )
            val response = apiService.getCardsByCategory(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Search topics
     */
    suspend fun searchTopics(query: String): Result<List<String>> = withContext(Dispatchers.IO) {
        try {
            // For now, return filtered list of predefined topics
            val topics = getAllTopics().filter { 
                it.contains(query, ignoreCase = true) 
            }
            Result.success(topics)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get all available financial topics
     */
    fun getAllTopics(): List<String> {
        return listOf(
            "Stock Market Basics",
            "Mutual Funds 101",
            "SIP Explained",
            "Emergency Fund",
            "50-30-20 Budgeting Rule",
            "Demat Account",
            "Trading vs Investing",
            "Term Insurance",
            "Health Insurance",
            "UPI Safety Tips",
            "PPF Benefits",
            "Retirement Planning",
            "NPS Explained",
            "Index Funds",
            "Blue Chip Stocks",
            "Dividend Stocks",
            "Portfolio Diversification",
            "Tax Saving Options",
            "Credit Card Management",
            "Loan Types",
            "CIBIL Score",
            "Real Estate Investment",
            "Gold Investment",
            "Cryptocurrency Basics",
            "SEBI Guidelines",
            "Banking Ombudsman",
            "Consumer Protection",
            "Digital Banking Security",
            "Fraud Prevention",
            "Savings Account vs FD"
        )
    }
}
