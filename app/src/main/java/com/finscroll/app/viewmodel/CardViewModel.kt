package com.finscroll.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finscroll.app.data.model.CardScreenState
import com.finscroll.app.data.model.FinancialCard
import com.finscroll.app.data.model.FinancialCategory
import com.finscroll.app.data.model.UserProgress
import com.finscroll.app.data.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val repository: CardRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CardScreenState())
    val uiState: StateFlow<CardScreenState> = _uiState.asStateFlow()
    
    private val _userProgress = MutableStateFlow(UserProgress())
    val userProgress: StateFlow<UserProgress> = _userProgress.asStateFlow()
    
    init {
        loadInitialCards()
    }
    
    /**
     * Load initial cards (Daily Mix)
     */
    fun loadInitialCards() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            repository.getDailyCards(count = 20).fold(
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            cardsList = response.questions,
                            currentCard = response.questions.firstOrNull(),
                            currentIndex = 0,
                            isLoading = false,
                            error = null,
                            showAnswer = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load cards"
                        )
                    }
                }
            )
        }
    }
    
    /**
     * Load cards by topic
     */
    fun loadCardsByTopic(topic: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            repository.fetchCards(topic = topic, count = 20).fold(
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            cardsList = response.questions,
                            currentCard = response.questions.firstOrNull(),
                            currentIndex = 0,
                            selectedTopic = topic,
                            isLoading = false,
                            error = null,
                            showAnswer = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load cards"
                        )
                    }
                }
            )
        }
    }
    
    /**
     * Load cards by category
     */
    fun loadCardsByCategory(category: FinancialCategory) {
        viewModelScope.launch {
            _uiState.update { 
                it.copy(
                    isLoading = true, 
                    error = null,
                    selectedCategory = category
                ) 
            }
            
            repository.getCardsByCategory(category = category, count = 20).fold(
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            cardsList = response.questions,
                            currentCard = response.questions.firstOrNull(),
                            currentIndex = 0,
                            isLoading = false,
                            error = null,
                            showAnswer = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load cards"
                        )
                    }
                }
            )
        }
    }
    
    /**
     * Move to next card
     */
    fun nextCard() {
        val state = _uiState.value
        val nextIndex = state.currentIndex + 1
        
        if (nextIndex < state.cardsList.size) {
            // Mark current card as viewed
            state.currentCard?.let { card ->
                _userProgress.update { progress ->
                    progress.copy(
                        cardsLearned = progress.cardsLearned + 1
                    )
                }
            }
            
            _uiState.update {
                it.copy(
                    currentIndex = nextIndex,
                    currentCard = it.cardsList[nextIndex],
                    showAnswer = false
                )
            }
            
            // Load more if near the end
            if (nextIndex >= state.cardsList.size - 5) {
                loadMoreCards()
            }
        }
    }
    
    /**
     * Move to previous card
     */
    fun previousCard() {
        val state = _uiState.value
        val prevIndex = state.currentIndex - 1
        
        if (prevIndex >= 0) {
            _uiState.update {
                it.copy(
                    currentIndex = prevIndex,
                    currentCard = it.cardsList[prevIndex],
                    showAnswer = false
                )
            }
        }
    }
    
    /**
     * Toggle answer visibility
     */
    fun toggleAnswer() {
        _uiState.update { it.copy(showAnswer = !it.showAnswer) }
    }
    
    /**
     * Bookmark current card
     */
    fun toggleBookmark() {
        val currentCard = _uiState.value.currentCard ?: return
        
        _userProgress.update { progress ->
            val bookmarks = progress.bookmarkedCards.toMutableSet()
            if (currentCard.id in bookmarks) {
                bookmarks.remove(currentCard.id)
            } else {
                bookmarks.add(currentCard.id)
            }
            progress.copy(bookmarkedCards = bookmarks)
        }
    }
    
    /**
     * Load more cards (pagination)
     */
    private fun loadMoreCards() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            
            val category = _uiState.value.selectedCategory
            val result = if (category != null) {
                repository.getCardsByCategory(category, count = 20)
            } else {
                repository.getDailyCards(count = 20)
            }
            
            result.fold(
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            cardsList = it.cardsList + response.questions,
                            isLoadingMore = false
                        )
                    }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoadingMore = false) }
                }
            )
        }
    }
    
    /**
     * Retry loading cards
     */
    fun retry() {
        if (_uiState.value.selectedCategory != null) {
            loadCardsByCategory(_uiState.value.selectedCategory!!)
        } else {
            loadInitialCards()
        }
    }
    
    /**
     * Update learning streak
     */
    fun updateStreak() {
        _userProgress.update { progress ->
            val now = System.currentTimeMillis()
            val lastLearned = progress.lastLearnedDate
            val oneDayMs = 24 * 60 * 60 * 1000L
            
            val newStreak = if (lastLearned == 0L) {
                1
            } else if (now - lastLearned < oneDayMs * 2) {
                progress.currentStreak + 1
            } else {
                1
            }
            
            progress.copy(
                currentStreak = newStreak,
                longestStreak = maxOf(newStreak, progress.longestStreak),
                lastLearnedDate = now
            )
        }
    }
}
