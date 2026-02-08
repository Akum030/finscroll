package com.finscroll.app.data.model

import com.google.gson.annotations.SerializedName

/**
 * Main data model for financial Q&A cards
 */
data class FinancialCard(
    @SerializedName("id")
    val id: String = "",
    
    @SerializedName("question")
    val question: String = "",
    
    @SerializedName("answer")
    val answer: String = "",
    
    @SerializedName("category")
    val category: FinancialCategory = FinancialCategory.GENERAL,
    
    @SerializedName("difficulty")
    val difficulty: Difficulty = Difficulty.MEDIUM,
    
    @SerializedName("tags")
    val tags: List<String> = emptyList(),
    
    @SerializedName("source")
    val source: String = "AI Generated",
    
    @SerializedName("relatedTopics")
    val relatedTopics: List<String> = emptyList(),
    
    @SerializedName("example")
    val example: String? = null,
    
    // Local fields (not from API)
    var isBookmarked: Boolean = false,
    var hasBeenViewed: Boolean = false,
    var lastViewedAt: Long = 0L
)

/**
 * Financial categories matching hackathon requirements
 */
enum class FinancialCategory(val displayName: String) {
    @SerializedName("savings")
    SAVINGS("Savings & Budgeting"),
    
    @SerializedName("investments")
    INVESTMENTS("Investments"),
    
    @SerializedName("insurance")
    INSURANCE("Insurance"),
    
    @SerializedName("digital_finance")
    DIGITAL_FINANCE("Digital Finance"),
    
    @SerializedName("retirement")
    RETIREMENT("Retirement Planning"),
    
    @SerializedName("consumer_rights")
    CONSUMER_RIGHTS("Consumer Rights"),
    
    @SerializedName("stock_market")
    STOCK_MARKET("Stock Market"),
    
    @SerializedName("mutual_funds")
    MUTUAL_FUNDS("Mutual Funds"),
    
    @SerializedName("general")
    GENERAL("General")
}

/**
 * Question difficulty levels
 */
enum class Difficulty(val displayName: String) {
    @SerializedName("easy")
    EASY("Easy"),
    
    @SerializedName("medium")
    MEDIUM("Medium"),
    
    @SerializedName("advanced")
    ADVANCED("Advanced")
}

/**
 * API Response wrapper
 */
data class CardResponse(
    @SerializedName("questions")
    val questions: List<FinancialCard> = emptyList(),
    
    @SerializedName("source")
    val source: String = "AI Generated",
    
    @SerializedName("topic")
    val topic: String = "",
    
    @SerializedName("totalQuestions")
    val totalQuestions: Int = 0,
    
    @SerializedName("hasMore")
    val hasMore: Boolean = false,
    
    @SerializedName("nextPage")
    val nextPage: Int? = null
)

/**
 * API Request for n8n webhook
 */
data class CardRequest(
    @SerializedName("topic")
    val topic: String,
    
    @SerializedName("count")
    val count: Int = 20,
    
    @SerializedName("category")
    val category: String? = null,
    
    @SerializedName("difficulty")
    val difficulty: String? = null
)

/**
 * Search response
 */
data class SearchTopicsResponse(
    @SerializedName("topics")
    val topics: List<FinancialTopic> = emptyList()
)

/**
 * Financial topic model
 */
data class FinancialTopic(
    @SerializedName("name")
    val name: String = "",
    
    @SerializedName("category")
    val category: FinancialCategory = FinancialCategory.GENERAL,
    
    @SerializedName("description")
    val description: String = "",
    
    @SerializedName("cardCount")
    val cardCount: Int = 0,
    
    @SerializedName("icon")
    val icon: String? = null
)

/**
 * User progress tracking
 */
data class UserProgress(
    val cardsLearned: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastLearnedDate: Long = 0L,
    val bookmarkedCards: Set<String> = emptySet(),
    val categoriesProgress: Map<FinancialCategory, Int> = emptyMap()
)

/**
 * UI State for card screen
 */
data class CardScreenState(
    val currentCard: FinancialCard? = null,
    val cardsList: List<FinancialCard> = emptyList(),
    val currentIndex: Int = 0,
    val showAnswer: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val selectedCategory: FinancialCategory? = null,
    val selectedTopic: String? = null,
    val selectedDifficulty: Difficulty? = null
)
