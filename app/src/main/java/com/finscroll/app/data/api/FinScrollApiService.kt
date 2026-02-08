package com.finscroll.app.data.api

import com.finscroll.app.data.model.CardRequest
import com.finscroll.app.data.model.CardResponse
import com.finscroll.app.data.model.SearchTopicsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Retrofit API interface for FinScroll backend (n8n webhook)
 */
interface FinScrollApiService {
    
    /**
     * Fetch financial literacy cards from n8n AI agent
     * POST endpoint for n8n webhook
     */
    @POST("webhook/finscroll")
    suspend fun fetchCards(
        @Body request: CardRequest
    ): CardResponse
    
    /**
     * Get daily mixed cards (various topics and difficulties)
     */
    @POST("webhook/finscroll/daily")
    suspend fun getDailyCards(
        @Body request: CardRequest
    ): CardResponse
    
    /**
     * Search available topics
     * Optional: If you implement topic search in n8n
     */
    @GET("webhook/finscroll/topics")
    suspend fun searchTopics(
        @Query("query") query: String
    ): SearchTopicsResponse
    
    /**
     * Get cards by category
     */
    @POST("webhook/finscroll")
    suspend fun getCardsByCategory(
        @Body request: CardRequest
    ): CardResponse
}
