# 🏆 HACKATHON APP - FinScroll: Financial Literacy Learning App

## COMPLETE IMPLEMENTATION PROMPT - COPY THIS ENTIRE FILE

---

# PROJECT OVERVIEW

Create a **financial literacy Android app** called "FinScroll" for a hackathon submission. The app gamifies financial learning through an infinite scroll experience (like Instagram/TikTok) where users scroll through bite-sized Q&A cards about stock market and financial concepts.

**Key Requirements:**
- ❌ **NO Authentication** - No login/signup, direct access
- 📱 **Infinite Scroll** - Swipe-based Q&A cards
- 💰 **Financial Literacy Focus** - Stock market, investments, budgeting, savings
- 🎮 **Gamified Learning** - Interactive, engaging, layman terms
- 🌐 **n8n Backend** - API endpoint with AI agent node
- 🎯 **Hackathon Compliant** - Covers 3+ financial themes

---

# HACKATHON REQUIREMENTS INTEGRATION

**Track D: Young Adult Focus**
- Savings and budgeting
- Investment basics (stocks, mutual funds, SIP)
- Digital finance safety
- Consumer rights and fraud prevention
- Retirement planning basics

**Financial Themes Covered (>3):**
1. ✅ **Savings & Budgeting** - Emergency fund, 50-30-20 rule
2. ✅ **Investments** - Stocks, mutual funds, SIP, diversification
3. ✅ **Insurance** - Life, health, term insurance basics
4. ✅ **Digital Finance** - UPI safety, online fraud prevention
5. ✅ **Retirement Planning** - PPF, NPS, EPF
6. ✅ **Consumer Rights** - Banking ombudsman, SEBI complaints

---

# ANDROID APP STRUCTURE

## App Name: **FinScroll**
## Package: `com.finscroll.app`
## Theme: Green-focused (money/growth theme)

---

# STEP 1: CREATE ANDROID PROJECT

```bash
# In Android Studio:
# 1. New Project → Empty Compose Activity
# 2. Name: FinScroll
# 3. Package: com.finscroll.app
# 4. Language: Kotlin
# 5. Minimum SDK: 26
```

---

# STEP 2: build.gradle.kts (Module: app)

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
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    
    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")
    
    // Lifecycle & ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    
    // Hilt Dependency Injection
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    
    // Networking - Retrofit & OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Image Loading - Coil
    implementation("io.coil-kt:coil-compose:2.5.0")
    
    // Splash Screen API
    implementation("androidx.core:core-splashscreen:1.0.1")
}

kapt {
    correctErrorTypes = true
}
```

---

# STEP 3: build.gradle.kts (Project level)

```kotlin
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}
```

---

# STEP 4: AndroidManifest.xml

**Location:** `app/src/main/AndroidManifest.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:name=".FinScrollApp"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.FinScroll"
        android:usesCleartextTraffic="true"
        tools:targetApi="34">
        
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.FinScroll">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

---

# STEP 5: strings.xml

**Location:** `app/src/main/res/values/strings.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">FinScroll</string>
</resources>
```

---

# STEP 6: themes.xml

**Location:** `app/src/main/res/values/themes.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="Theme.FinScroll" parent="android:Theme.Material.Light.NoActionBar">
        <item name="android:statusBarColor">@android:color/transparent</item>
        <item name="android:navigationBarColor">@android:color/transparent</item>
    </style>
</resources>
```

---

# STEP 7: Color.kt (Theme Colors)

**Location:** `app/src/main/java/com/finscroll/app/ui/theme/Color.kt`

```kotlin
package com.finscroll.app.ui.theme

import androidx.compose.ui.graphics.Color

// Financial Green Theme
val Primary = Color(0xFF00C853)        // Green
val PrimaryDark = Color(0xFF00994D)
val PrimaryLight = Color(0xFF69F0AE)
val PrimaryContainer = Color(0xFFE8F5E9)
val OnPrimaryContainer = Color(0xFF002106)

val Secondary = Color(0xFFFFD700)      // Gold
val SecondaryDark = Color(0xFFFFA000)
val SecondaryLight = Color(0xFFFFE082)
val SecondaryContainer = Color(0xFFFFF9C4)
val OnSecondaryContainer = Color(0xFF4A3C00)

val Tertiary = Color(0xFF2196F3)       // Blue
val TertiaryDark = Color(0xFF1976D2)
val TertiaryLight = Color(0xFF64B5F6)

val Background = Color(0xFFF5F9F5)
val OnBackground = Color(0xFF1A1C1A)
val Surface = Color.White
val OnSurface = Color(0xFF1A1C1A)
val SurfaceVariant = Color(0xFFE0F2E0)
val OnSurfaceVariant = Color(0xFF424940)

val Error = Color(0xFFBA1A1A)
val ErrorContainer = Color(0xFFFFDAD6)
val CardBorder = Color(0xFFE0E0E0)

// Dark theme
val BackgroundDark = Color(0xFF1A1C1A)
val OnBackgroundDark = Color(0xFFE2E3DD)
val SurfaceDark = Color(0xFF1F1F1F)
val OnSurfaceDark = Color(0xFFE2E3DD)
val SurfaceVariantDark = Color(0xFF424940)
```

---

# STEP 8: Theme.kt

**Location:** `app/src/main/java/com/finscroll/app/ui/theme/Theme.kt`

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
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    error = Error,
    onError = Color.White,
    errorContainer = ErrorContainer,
    outline = CardBorder
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
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariant,
    error = Error,
    onError = Color.White,
    errorContainer = ErrorContainer,
    outline = SurfaceVariantDark
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

# STEP 9: Typography.kt

**Location:** `app/src/main/java/com/finscroll/app/ui/theme/Type.kt`

```kotlin
package com.finscroll.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
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
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
)
```

---

# CONTINUE TO NEXT PART FOR DATA MODELS AND API...

**[This is Part 1 of the comprehensive prompt. Due to length, I'll provide this in a file format]**

---

# N8N FLOW PROMPTS

## AI Agent System Prompt for Financial Literacy

```
You are a financial literacy expert for young Indian adults. Your goal is to make stock market and personal finance concepts easy to understand through engaging Q&A format.

Context: This is for a gamified learning app called FinScroll where users scroll through financial knowledge cards.

Guidelines:
1. Use simple, layman terms - avoid jargon
2. Use Indian context (INR, NSE, BSE, SEBI, Indian banks)
3. Keep answers concise (2-3 sentences max)
4. Include practical examples from daily life
5. Focus on actionable knowledge
6. Cover these themes: savings, budgeting, stock market, mutual funds, insurance, digital finance, fraud prevention

Question Style:
- "What is [concept]?"
- "How does [process] work?"
- "Why should I [action]?"
- "What is the difference between [X] and [Y]?"

Example Output Format:
{
  "questions": [
    {
      "question": "What is SIP in mutual funds?",
      "answer": "SIP (Systematic Investment Plan) is like a recurring deposit for mutual funds. You invest a fixed amount every month (₹500, ₹1000, etc.) instead of a lump sum. This helps you build wealth slowly without timing the market."
    }
  ]
}
```

## n8n AI Agent Node User Prompt Template

```
Generate {count} financial literacy questions and answers about: {topic}

Topic: {topic}
Number of questions: {count}

Requirements:
- Use simple Indian English
- Include Indian examples (₹, Nifty, Sensex, SBI, ICICI, etc.)
- Mix difficulty: 30% easy, 50% medium, 20% advanced
- Cover practical scenarios
- Avoid complex financial jargon
- Each answer should be 2-3 sentences

Output JSON format:
{
  "questions": [
    {
      "question": "string",
      "answer": "string"
    }
  ],
  "source": "AI Generated",
  "topic": "{topic}",
  "totalQuestions": {count}
}
```

---

# SAMPLE FINANCIAL TOPICS

```kotlin
val financialTopics = listOf(
    // Basics
    "Stock Market Basics",
    "Mutual Funds 101",
    "SIP Explained",
    "Demat Account",
    "Trading vs Investing",
    
    // Savings & Budgeting
    "Emergency Fund",
    "50-30-20 Rule",
    "Savings Account vs FD",
    "PPF Benefits",
    "Tax Saving Options",
    
    // Investments
    "Equity vs Debt",
    "Index Funds",
    "Blue Chip Stocks",
    "Dividend Stocks",
    "Portfolio Diversification",
    
    // Insurance
    "Term Insurance",
    "Health Insurance",
    "Life Insurance vs ULIP",
    
    // Digital Finance
    "UPI Safety Tips",
    "Online Banking Security",
    "Credit Card Fraud Prevention",
    "Phishing Scams",
    
    // Advanced
    "Retirement Planning",
    "NPS Benefits",
    "Capital Gains Tax",
    "Stock Market Indicators"
)
```

---

**CONTINUE IN FINSCROLL_COMPLETE_PROMPT.md FOR ALL CODE FILES...**
