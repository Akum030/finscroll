# 🚀 FINSCROLL - COMPLETE COPY-PASTE IMPLEMENTATION

## ⚡ QUICK START INSTRUCTIONS

**Copy this entire file and paste it in a new Claude/ChatGPT session with this prompt:**

```
I need you to create a complete Android app based on the specifications below. 
Create ALL files exactly as specified. The app is called FinScroll - a financial 
literacy learning app with infinite scroll Q&A cards (like TikTok for finance).
NO authentication, direct access, swipe-based learning. 

Please implement everything step-by-step without asking questions.
```

---

# 📋 PROJECT SPECIFICATIONS

## App Details
- **Name:** FinScroll
- **Package:** com.finscroll.app
- **Theme:** Green (financial/money theme)
- **Target:** Young adults (18-30) in India
- **Hackathon:** Financial Literacy Gamification

## Features
✅ NO authentication (no login/signup)  
✅ Infinite scroll Q&A cards  
✅ Swipe gestures (TikTok-style)  
✅ Search financial topics  
✅ Bookmark important questions  
✅ Daily learning streak tracker  
✅ Progress indicators  
✅ Offline mode with cached questions  

## Financial Themes Covered (Hackathon Requirement)
1. **Savings & Budgeting** - Emergency fund, 50-30-20 rule
2. **Investments** - Stocks, mutual funds, SIP, diversification
3. **Insurance** - Term, health, life insurance basics
4. **Digital Finance** - UPI safety, fraud prevention
5. **Retirement Planning** - PPF, NPS, EPF
6. **Consumer Rights** - SEBI, banking ombudsman

---

# 🏗️ PROJECT STRUCTURE

```
FinScroll/
├── app/
│   ├── build.gradle.kts
│   ├── src/
│   │   └── main/
│   │       ├── AndroidManifest.xml
│   │       ├── java/com/finscroll/app/
│   │       │   ├── FinScrollApp.kt                    # Application class
│   │       │   ├── MainActivity.kt                    # Main entry
│   │       │   ├── di/
│   │       │   │   └── NetworkModule.kt              # Hilt DI
│   │       │   ├── data/
│   │       │   │   ├── model/
│   │       │   │   │   └── FinancialCard.kt          # Data models
│   │       │   │   ├── api/
│   │       │   │   │   └── FinScrollApiService.kt    # Retrofit API
│   │       │   │   └── repository/
│   │       │   │       └── CardRepository.kt          # Data repository
│   │       │   ├── ui/
│   │       │   │   ├── screens/
│   │       │   │   │   ├── HomeScreen.kt             # Main scroll screen
│   │       │   │   │   ├── SearchScreen.kt           # Search topics
│   │       │   │   │   └── ProgressScreen.kt         # Learning stats
│   │       │   │   ├── components/
│   │       │   │   │   ├── FinancialCard.kt          # Q&A card UI
│   │       │   │   │   └── SwipeHandler.kt           # Swipe gestures
│   │       │   │   └── theme/
│   │       │   │       ├── Color.kt                   # Theme colors
│   │       │   │       ├── Theme.kt                   # Material theme
│   │       │   │       └── Type.kt                    # Typography
│   │       │   └── viewmodel/
│   │       │       └── CardViewModel.kt               # ViewModel
│   │       └── res/
│   │           ├── values/
│   │           │   ├── strings.xml
│   │           │   └── themes.xml
│   │           └── drawable/                          # Icons
├── build.gradle.kts                                    # Project gradle
└── settings.gradle.kts
```

---

# 📦 STEP 1: Project-Level build.gradle.kts

**File:** `build.gradle.kts` (root)

```kotlin
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
```

---

# 📦 STEP 2: settings.gradle.kts

**File:** `settings.gradle.kts` (root)

```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "FinScroll"
include(":app")
```

---

# 📦 STEP 3: App-Level build.gradle.kts

**File:** `app/build.gradle.kts`

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.finscroll.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.finscroll.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
    
    buildFeatures {
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    
    // Compose
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation("androidx.compose.ui:ui-tooling")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")
    
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    
    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    
    // Retrofit & Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Image Loading
    implementation("io.coil-kt:coil-compose:2.5.0")
    
    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")
    
    // DataStore for preferences
    implementation("androidx.datastore:datastore-preferences:1.0.0")
}

kapt {
    correctErrorTypes = true
}
```

---

# 📱 STEP 4: AndroidManifest.xml

**File:** `app/src/main/AndroidManifest.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:name=".FinScrollApp"
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.FinScroll"
        android:usesCleartextTraffic="true"
        tools:targetApi="31">
        
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.FinScroll"
            android:windowSoftInputMode="adjustResize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

---

# 📱 STEP 5: strings.xml

**File:** `app/src/main/res/values/strings.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">FinScroll</string>
    <string name="tagline">Scroll Your Way to Financial Freedom</string>
    <string name="search_hint">Search financial topics…</string>
    <string name="home">Home</string>
    <string name="search">Search</string>
    <string name="progress">Progress</string>
    <string name="bookmark">Bookmark</string>
    <string name="share">Share</string>
    <string name="next_card">Next Card</string>
    <string name="prev_card">Previous Card</string>
    <string name="loading">Loading financial wisdom…</string>
    <string name="error_loading">Failed to load questions</string>
    <string name="retry">Retry</string>
    <string name="no_internet">No internet connection</string>
    <string name="swipe_up">Swipe up for next question</string>
    <string name="learning_streak">Day Streak</string>
    <string name="cards_learned">Cards Learned</string>
    <string name="topics_covered">Topics Covered</string>
</resources>
```

---

# 🎨 STEP 6: themes.xml

**File:** `app/src/main/res/values/themes.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="Theme.FinScroll" parent="android:Theme.Material.Light.NoActionBar">
        <item name="android:statusBarColor">@android:color/transparent</item>
        <item name="android:navigationBarColor">@android:color/transparent</item>
        <item name="android:windowLightStatusBar">true</item>
    </style>
</resources>
```

---

# 🎨 STEP 7: Color.kt

**File:** `app/src/main/java/com/finscroll/app/ui/theme/Color.kt`

```kotlin
package com.finscroll.app.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Green Palette (Financial/Money Theme)
val Primary = Color(0xFF00C853)              // Bright Green
val PrimaryDark = Color(0xFF00994D)          // Dark Green
val PrimaryLight = Color(0xFF69F0AE)         // Light Green
val PrimaryContainer = Color(0xFFE8F5E9)     // Very Light Green
val OnPrimaryContainer = Color(0xFF002106)   // Dark Text on Light Green

// Secondary Gold Palette (Wealth/Success Theme)
val Secondary = Color(0xFFFFD700)            // Gold
val SecondaryDark = Color(0xFFFFA000)        // Dark Gold
val SecondaryLight = Color(0xFFFFE082)       // Light Gold
val SecondaryContainer = Color(0xFFFFF9C4)   // Very Light Gold
val OnSecondaryContainer = Color(0xFF4A3C00) // Dark Text on Light Gold

// Tertiary Blue Palette (Trust/Stability)
val Tertiary = Color(0xFF2196F3)             // Blue
val TertiaryDark = Color(0xFF1976D2)         // Dark Blue
val TertiaryLight = Color(0xFF64B5F6)        // Light Blue
val TertiaryContainer = Color(0xFFE3F2FD)    // Very Light Blue
val OnTertiaryContainer = Color(0xFF001D35)  // Dark Text on Light Blue

// Neutral Colors
val Background = Color(0xFFF5F9F5)           // Off-White with Green Tint
val OnBackground = Color(0xFF1A1C1A)         // Almost Black
val Surface = Color.White                    // Pure White
val OnSurface = Color(0xFF1A1C1A)            // Almost Black
val SurfaceVariant = Color(0xFFE0F2E0)       // Light Green Variant
val OnSurfaceVariant = Color(0xFF424940)     // Dark Gray

// Semantic Colors
val Error = Color(0xFFBA1A1A)                // Red Error
val ErrorContainer = Color(0xFFFFDAD6)       // Light Red
val OnError = Color.White                    // White Text on Red
val OnErrorContainer = Color(0xFF410002)     // Dark Text on Light Red

val Success = Color(0xFF00C853)              // Green Success
val Warning = Color(0xFFFFB300)              // Orange Warning
val Info = Color(0xFF2196F3)                 // Blue Info

// Card & Border Colors
val CardBorder = Color(0xFFE0E0E0)           // Light Gray Border
val Divider = Color(0xFFE0E0E0)              // Divider Line

// Dark Theme Colors
val BackgroundDark = Color(0xFF1A1C1A)
val OnBackgroundDark = Color(0xFFE2E3DD)
val SurfaceDark = Color(0xFF1F1F1F)
val OnSurfaceDark = Color(0xFFE2E3DD)
val SurfaceVariantDark = Color(0xFF424940)
val OnSurfaceVariantDark = Color(0xFFC2C8BE)
```

---

# 🎨 STEP 8: Type.kt

**File:** `app/src/main/java/com/finscroll/app/ui/theme/Type.kt`

```kotlin
package com.finscroll.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    // Display styles
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),
    
    // Headline styles
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    
    // Title styles
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    
    // Body styles
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    
    // Label styles
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
```

---

# 🎨 STEP 9: Theme.kt

**File:** `app/src/main/java/com/finscroll/app/ui/theme/Theme.kt`

```kotlin
package com.finscroll.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = Color.White,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = Color.White,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    outline = CardBorder,
    outlineVariant = Divider
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLight,
    onPrimary = PrimaryDark,
    primaryContainer = PrimaryDark,
    onPrimaryContainer = PrimaryContainer,
    secondary = SecondaryLight,
    onSecondary = SecondaryDark,
    secondaryContainer = SecondaryDark,
    onSecondaryContainer = SecondaryContainer,
    tertiary = TertiaryLight,
    onTertiary = TertiaryDark,
    tertiaryContainer = TertiaryDark,
    onTertiaryContainer = TertiaryContainer,
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = SurfaceVariantDark,
    outlineVariant = SurfaceVariantDark
)

@Composable
fun FinScrollTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

---

# 📊 STEP 10: Data Models

**File:** `app/src/main/java/com/finscroll/app/data/model/FinancialCard.kt`

```kotlin
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
    
    @SerializedName("topic")
    val topic: String = "",
    
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
    
    // Local fields (not from API)
    var isBookmarked: Boolean = false,
    var viewCount: Int = 0,
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
    val source: String = "",
    
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
    val count: Int = 10,
    
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
    val name: String,
    
    @SerializedName("category")
    val category: FinancialCategory,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("totalCards")
    val totalCards: Int = 0,
    
    @SerializedName("icon")
    val icon: String? = null
)

/**
 * User progress tracking
 */
data class UserProgress(
    val cardsLearned: Int = 0,
    val currentStreak: Int = 0,
    val topicsCovered: Set<String> = emptySet(),
    val bookmarkedCards: Set<String> = emptySet(),
    val lastActiveDate: Long = 0L,
    val categoriesProgress: Map<FinancialCategory, Int> = emptyMap()
)

/**
 * UI State for card screen
 */
data class CardScreenState(
    val currentCard: FinancialCard? = null,
    val cardsList: List<FinancialCard> = emptyList(),
    val currentIndex: Int = 0,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = true,
    val showAnswer: Boolean = false,
    val selectedCategory: FinancialCategory? = null,
    val selectedDifficulty: Difficulty? = null
)
```

---

# 🌐 STEP 11: API Service

**File:** `app/src/main/java/com/finscroll/app/data/api/FinScrollApiService.kt`

```kotlin
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
     * Get financial Q&A cards from n8n webhook
     * 
     * Usage:
     * POST https://your-n8n-url.com/webhook/finscroll
     * Body: { "topic": "Stock Market Basics", "count": 10 }
     */
    @POST("webhook/finscroll")
    suspend fun getCards(
        @Body request: CardRequest
    ): CardResponse
    
    /**
     * Search topics (if you want to add this later)
     */
    @GET("webhook/finscroll/search")
    suspend fun searchTopics(
        @Query("query") query: String
    ): SearchTopicsResponse
    
    /**
     * Get random daily cards
     */
    @POST("webhook/finscroll/daily")
    suspend fun getDailyCards(
        @Body request: CardRequest
    ): CardResponse
}
```

---

# 🔧 STEP 12: Network Module (Hilt DI)

**File:** `app/src/main/java/com/finscroll/app/di/NetworkModule.kt`

```kotlin
package com.finscroll.app.di

import com.finscroll.app.data.api.FinScrollApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    /**
     * IMPORTANT: Replace this with your n8n webhook URL
     * 
     * Example:
     * - Local testing: "http://10.0.2.2:5678/" (Android emulator to localhost)
     * - Production: "https://your-n8n-instance.com/"
     */
    private const val BASE_URL = "https://n8n.backend.lehana.in/"
    
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    @Provides
    @Singleton
    fun provideFinScrollApiService(retrofit: Retrofit): FinScrollApiService {
        return retrofit.create(FinScrollApiService::class.java)
    }
}
```

---

# 📦 STEP 13: Repository

**File:** `app/src/main/java/com/finscroll/app/data/repository/CardRepository.kt`

```kotlin
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
    
    // Cache for offline support
    private val cardCache = mutableMapOf<String, List<FinancialCard>>()
    
    /**
     * Fetch financial cards from API
     */
    suspend fun fetchCards(
        topic: String,
        count: Int = 10,
        category: FinancialCategory? = null
    ): Result<CardResponse> = withContext(Dispatchers.IO) {
        try {
            val request = CardRequest(
                topic = topic,
                count = count,
                category = category?.name?.lowercase()
            )
            
            val response = apiService.getCards(request)
            
            // Cache the results
            cardCache[topic] = response.questions
            
            Result.success(response)
        } catch (e: Exception) {
            // Try to return cached data on error
            val cachedCards = cardCache[topic]
            if (cachedCards != null) {
                Result.success(
                    CardResponse(
                        questions = cachedCards,
                        source = "Cache",
                        topic = topic,
                        totalQuestions = cachedCards.size
                    )
                )
            } else {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Get random daily cards
     */
    suspend fun getDailyCards(count: Int = 10): Result<CardResponse> = withContext(Dispatchers.IO) {
        try {
            val request = CardRequest(
                topic = "Daily Mix",
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
        count: Int = 10
    ): Result<CardResponse> {
        return fetchCards(
            topic = category.displayName,
            count = count,
            category = category
        )
    }
    
    /**
     * Clear cache
     */
    fun clearCache() {
        cardCache.clear()
    }
}
```

---

**[TO BE CONTINUED - FILE SIZE LIMIT]**

# 🚨 NEXT SECTIONS TO INCLUDE:

## Part 2 Contents:
- ViewModel (CardViewModel.kt)
- MainActivity.kt
- FinScrollApp.kt (Application class)
- HomeScreen.kt (Main swipe interface)
- FinancialCard.kt (Card UI component)
- SwipeHandler.kt (Gesture handling)
- SearchScreen.kt
- ProgressScreen.kt
- Navigation setup

## Part 3 - N8N Configuration:
- Complete n8n flow setup
- AI Agent node configuration
- Webhook configuration
- Sample prompts
- Response format examples

---

# ⚡ QUICK N8N SETUP PREVIEW

**N8N Flow Structure:**
```
Webhook Trigger → AI Agent Node → Response Node
```

**AI Agent System Prompt:**
```
You are FinScroll AI - a financial literacy expert for young Indian adults (18-30).

Mission: Generate engaging Q&A cards about personal finance, stock market, and investing in simple layman terms using Indian context.

Output Format (JSON):
{
  "questions": [
    {
      "id": "uuid",
      "question": "What is SIP?",
      "answer": "SIP (Systematic Investment Plan) is like a recurring deposit for mutual funds. You invest ₹500-₹5000 every month automatically. It helps build wealth slowly through market averaging.",
      "topic": "Mutual Funds",
      "category": "investments",
      "difficulty": "easy",
      "tags": ["sip", "mutual funds", "investments"],
      "source": "AI Generated"
    }
  ],
  "totalQuestions": 10
}

Rules:
- Use Indian examples (₹, NSE, BSE, SEBI, SBI, ICICI)
- Keep answers 2-3 sentences max
- Use simple words, avoid jargon
- Mix 30% easy, 50% medium, 20% advanced
- Cover: savings, investments, insurance, digital finance, consumer rights
```

---

**Would you like me to continue with:**
1. ✅ Part 2 - All remaining Kotlin code (ViewModel, UI screens, components)
2. ✅ Part 3 - Complete n8n flow setup guide
3. ✅ Part 4 - Testing & deployment instructions
4. ✅ Create separate focused files for each part

**Reply with: "continue" and I'll create all remaining files!**
