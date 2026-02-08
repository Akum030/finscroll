# FINSCROLL - PART 2: UI COMPONENTS & VIEWMODEL

---

# 🧠 STEP 14: CardViewModel

**File:** `app/src/main/java/com/finscroll/app/viewmodel/CardViewModel.kt`

```kotlin
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
                            hasMore = response.hasMore
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
                            isLoading = false,
                            hasMore = response.hasMore
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
                            hasMore = response.hasMore
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
                        cardsLearned = progress.cardsLearned + 1,
                        topicsCovered = progress.topicsCovered + card.topic
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
            if (nextIndex >= state.cardsList.size - 3 && state.hasMore && !state.isLoadingMore) {
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
            if (bookmarks.contains(currentCard.id)) {
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
            
            val topic = _uiState.value.selectedCategory?.displayName ?: "Daily Mix"
            
            repository.fetchCards(topic = topic, count = 10).fold(
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            cardsList = it.cardsList + response.questions,
                            isLoadingMore = false,
                            hasMore = response.hasMore
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
            val lastActive = progress.lastActiveDate
            
            // Check if it's a new day
            val oneDayMs = 24 * 60 * 60 * 1000
            val isNewDay = now - lastActive > oneDayMs
            
            val newStreak = if (isNewDay) {
                // Check if it's consecutive day
                if (now - lastActive < 2 * oneDayMs) {
                    progress.currentStreak + 1
                } else {
                    1 // Reset streak
                }
            } else {
                progress.currentStreak
            }
            
            progress.copy(
                currentStreak = newStreak,
                lastActiveDate = now
            )
        }
    }
}
```

---

# 🏠 STEP 15: MainActivity

**File:** `app/src/main/java/com/finscroll/app/MainActivity.kt`

```kotlin
package com.finscroll.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.finscroll.app.ui.navigation.FinScrollNavigation
import com.finscroll.app.ui.theme.FinScrollTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        setContent {
            FinScrollTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FinScrollNavigation()
                }
            }
        }
    }
}
```

---

# 🏗️ STEP 16: Application Class

**File:** `app/src/main/java/com/finscroll/app/FinScrollApp.kt`

```kotlin
package com.finscroll.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FinScrollApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize any required libraries here
        // Example: Analytics, Crash reporting, etc.
    }
}
```

---

# 🧭 STEP 17: Navigation

**File:** `app/src/main/java/com/finscroll/app/ui/navigation/Navigation.kt`

```kotlin
package com.finscroll.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.finscroll.app.ui.screens.HomeScreen
import com.finscroll.app.ui.screens.ProgressScreen
import com.finscroll.app.ui.screens.SearchScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Progress : Screen("progress")
}

@Composable
fun FinScrollNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToProgress = { navController.navigate(Screen.Progress.route) }
            )
        }
        
        composable(Screen.Search.route) {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Progress.route) {
            ProgressScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
```

---

# 🏠 STEP 18: HomeScreen (Main Swipe Interface)

**File:** `app/src/main/java/com/finscroll/app/ui/screens/HomeScreen.kt`

```kotlin
package com.finscroll.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.finscroll.app.ui.components.FinancialCardView
import com.finscroll.app.viewmodel.CardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToProgress: () -> Unit,
    viewModel: CardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val userProgress by viewModel.userProgress.collectAsState()
    
    var dragOffset by remember { mutableStateOf(0f) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "FinScroll",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Scroll Your Way to Financial Freedom",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    // Search button
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                    
                    // Progress button with streak badge
                    Box {
                        IconButton(onClick = onNavigateToProgress) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = "Progress"
                            )
                        }
                        if (userProgress.currentStreak > 0) {
                            Badge(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(6.dp)
                            ) {
                                Text(
                                    text = "${userProgress.currentStreak}",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingState()
                }
                
                uiState.error != null -> {
                    ErrorState(
                        error = uiState.error!!,
                        onRetry = { viewModel.retry() }
                    )
                }
                
                uiState.currentCard != null -> {
                    // Main card content with swipe gestures
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectVerticalDragGestures(
                                    onDragEnd = {
                                        if (dragOffset < -100) {
                                            // Swipe up - next card
                                            viewModel.nextCard()
                                        } else if (dragOffset > 100) {
                                            // Swipe down - previous card
                                            viewModel.previousCard()
                                        }
                                        dragOffset = 0f
                                    },
                                    onVerticalDrag = { _, dragAmount ->
                                        dragOffset += dragAmount
                                    }
                                )
                            }
                    ) {
                        FinancialCardView(
                            card = uiState.currentCard!!,
                            showAnswer = uiState.showAnswer,
                            onToggleAnswer = { viewModel.toggleAnswer() },
                            onBookmark = { viewModel.toggleBookmark() },
                            isBookmarked = userProgress.bookmarkedCards.contains(
                                uiState.currentCard!!.id
                            )
                        )
                    }
                    
                    // Navigation hints
                    AnimatedVisibility(
                        visible = !uiState.showAnswer,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) {
                        SwipeHint()
                    }
                    
                    // Loading more indicator
                    if (uiState.isLoadingMore) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                        )
                    }
                }
                
                else -> {
                    EmptyState(onRetry = { viewModel.loadInitialCards() })
                }
            }
        }
    }
    
    // Update streak when screen is shown
    LaunchedEffect(Unit) {
        viewModel.updateStreak()
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Loading financial wisdom…",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ErrorState(
    error: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = error,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(onClick = onRetry) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Retry")
            }
        }
    }
}

@Composable
fun EmptyState(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = "Empty",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "No cards available",
                style = MaterialTheme.typography.titleMedium
            )
            Button(onClick = onRetry) {
                Text("Load Cards")
            }
        }
    }
}

@Composable
fun SwipeHint() {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SwipeUp,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Swipe up for next • Tap to reveal answer",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
```

---

# 🃏 STEP 19: FinancialCard Component

**File:** `app/src/main/java/com/finscroll/app/ui/components/FinancialCardView.kt`

```kotlin
package com.finscroll.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.finscroll.app.data.model.Difficulty
import com.finscroll.app.data.model.FinancialCard
import com.finscroll.app.ui.theme.Success
import com.finscroll.app.ui.theme.Warning

@Composable
fun FinancialCardView(
    card: FinancialCard,
    showAnswer: Boolean,
    onToggleAnswer: () -> Unit,
    onBookmark: () -> Unit,
    isBookmarked: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable { onToggleAnswer() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header with category and difficulty
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category chip
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = card.category.displayName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                
                // Difficulty badge
                DifficultyBadge(difficulty = card.difficulty)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Question
            Text(
                text = card.question,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Divider
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Answer section
            AnimatedVisibility(
                visible = showAnswer,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column {
                    // Answer label
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Answer",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Answer text
                    Text(
                        text = card.answer,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Tags
                    if (card.tags.isNotEmpty()) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            card.tags.take(5).forEach { tag ->
                                TagChip(tag = tag)
                            }
                        }
                    }
                }
            }
            
            // Show answer hint when not visible
            AnimatedVisibility(
                visible = !showAnswer,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.TouchApp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Tap to reveal answer",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Bookmark button
                IconButton(onClick = onBookmark) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Share button
                IconButton(onClick = { /* TODO: Share functionality */ }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun DifficultyBadge(difficulty: Difficulty) {
    val (color, icon) = when (difficulty) {
        Difficulty.EASY -> Success to Icons.Default.CheckCircle
        Difficulty.MEDIUM -> Warning to Icons.Default.Info
        Difficulty.ADVANCED -> MaterialTheme.colorScheme.error to Icons.Default.Star
    }
    
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = difficulty.displayName,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Composable
fun TagChip(tag: String) {
    Surface(
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = "#$tag",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
```

---

**[File size limit - Continue to Part 3 for SearchScreen, ProgressScreen, and N8N configuration]**

---

# ✅ FILES COMPLETED SO FAR:

1. ✅ Build configuration (Gradle files)
2. ✅ AndroidManifest.xml
3. ✅ Resources (strings, themes)
4. ✅ Theme (Color, Type, Theme files)
5. ✅ Data Models
6. ✅ API Service
7. ✅ Hilt DI Network Module
8. ✅ Repository
9. ✅ ViewModel
10. ✅ MainActivity
11. ✅ Application Class
12. ✅ Navigation
13. ✅ HomeScreen (Main swipe interface)
14. ✅ FinancialCardView Component

---

# 🚀 STILL TO CREATE:

- SearchScreen.kt
- ProgressScreen.kt
- N8N Complete Setup Guide
- Testing & Deployment Instructions

**Reply "continue part 3" to get the remaining files!**
