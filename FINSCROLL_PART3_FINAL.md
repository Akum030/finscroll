# FINSCROLL - PART 3: REMAINING SCREENS & N8N SETUP

---

# 🔍 STEP 20: SearchScreen

**File:** `app/src/main/java/com/finscroll/app/ui/screens/SearchScreen.kt`

```kotlin
package com.finscroll.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.finscroll.app.data.model.FinancialCategory
import com.finscroll.app.viewmodel.CardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    viewModel: CardViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explore Topics") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search bar
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            
            // Categories list
            Text(
                text = "Financial Topics",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(getFilteredCategories(searchQuery)) { category ->
                    CategoryCard(
                        category = category,
                        onClick = {
                            viewModel.loadCardsByCategory(category)
                            onNavigateBack()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Search topics...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        },
        singleLine = true,
        shape = MaterialTheme.shapes.medium
    )
}

@Composable
fun CategoryCard(
    category: FinancialCategory,
    onClick: () -> Unit
) {
    val icon = when (category) {
        FinancialCategory.SAVINGS -> Icons.Default.Savings
        FinancialCategory.INVESTMENTS -> Icons.Default.TrendingUp
        FinancialCategory.INSURANCE -> Icons.Default.Security
        FinancialCategory.DIGITAL_FINANCE -> Icons.Default.PhoneAndroid
        FinancialCategory.RETIREMENT -> Icons.Default.ElderlyWoman
        FinancialCategory.CONSUMER_RIGHTS -> Icons.Default.Gavel
        FinancialCategory.STOCK_MARKET -> Icons.Default.ShowChart
        FinancialCategory.MUTUAL_FUNDS -> Icons.Default.AccountBalance
        else -> Icons.Default.AttachMoney
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.size(56.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            
            // Text
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = getCategoryDescription(category),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Arrow
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

fun getFilteredCategories(query: String): List<FinancialCategory> {
    val allCategories = FinancialCategory.values().toList()
    
    if (query.isBlank()) return allCategories
    
    return allCategories.filter {
        it.displayName.contains(query, ignoreCase = true)
    }
}

fun getCategoryDescription(category: FinancialCategory): String {
    return when (category) {
        FinancialCategory.SAVINGS -> "Learn about emergency funds, budgeting, and saving strategies"
        FinancialCategory.INVESTMENTS -> "Explore stocks, bonds, mutual funds, and SIP"
        FinancialCategory.INSURANCE -> "Understand term, life, and health insurance"
        FinancialCategory.DIGITAL_FINANCE -> "UPI safety, online banking, and fraud prevention"
        FinancialCategory.RETIREMENT -> "PPF, NPS, EPF, and retirement planning"
        FinancialCategory.CONSUMER_RIGHTS -> "Banking ombudsman, SEBI, and complaint redressal"
        FinancialCategory.STOCK_MARKET -> "NSE, BSE, trading, and market basics"
        FinancialCategory.MUTUAL_FUNDS -> "SIP, NAV, fund types, and investment strategies"
        else -> "General financial literacy concepts"
    }
}
```

---

# 📊 STEP 21: ProgressScreen

**File:** `app/src/main/java/com/finscroll/app/ui/screens/ProgressScreen.kt`

```kotlin
package com.finscroll.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.finscroll.app.viewmodel.CardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    onNavigateBack: () -> Unit,
    viewModel: CardViewModel = hiltViewModel()
) {
    val userProgress by viewModel.userProgress.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Progress") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header card with main stats
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Streak
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${userProgress.currentStreak}",
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Day Streak",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Keep learning daily to maintain your streak!",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            
            // Stats grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.School,
                        value = "${userProgress.cardsLearned}",
                        label = "Cards\nLearned"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Topic,
                        value = "${userProgress.topicsCovered.size}",
                        label = "Topics\nCovered"
                    )
                }
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Bookmark,
                        value = "${userProgress.bookmarkedCards.size}",
                        label = "Bookmarks"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.EmojiEvents,
                        value = "${calculateLevel(userProgress.cardsLearned)}",
                        label = "Level"
                    )
                }
            }
            
            // Topics covered section
            item {
                Text(
                    text = "Topics You've Explored",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            
            items(userProgress.topicsCovered.toList()) { topic ->
                TopicCard(topic = topic)
            }
            
            // Motivational message
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.TipsAndUpdates,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = getMotivationalMessage(userProgress.cardsLearned),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TopicCard(topic: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = topic,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

fun calculateLevel(cardsLearned: Int): Int {
    return (cardsLearned / 10) + 1
}

fun getMotivationalMessage(cardsLearned: Int): String {
    return when {
        cardsLearned == 0 -> "Start your financial literacy journey today!"
        cardsLearned < 10 -> "Great start! Keep scrolling to learn more."
        cardsLearned < 50 -> "You're building good financial knowledge!"
        cardsLearned < 100 -> "Impressive! You're becoming financially literate."
        cardsLearned < 500 -> "You're a financial knowledge champion!"
        else -> "Master level! Share your knowledge with others."
    }
}
```

---

# 🌐 N8N COMPLETE SETUP GUIDE

## N8N WORKFLOW CONFIGURATION

### 🔧 Step 1: Create N8N Workflow

1. **Create New Workflow** in your n8n instance
2. **Name:** FinScroll Financial Literacy API

---

### 📥 Step 2: Add Webhook Trigger Node

**Node:** `Webhook`

**Configuration:**
```json
{
  "name": "FinScroll Webhook",
  "type": "n8n-nodes-base.webhook",
  "position": [250, 300],
  "webhookId": "finscroll-api",
  "parameters": {
    "path": "webhook/finscroll",
    "httpMethod": "POST",
    "responseMode": "responseNode",
    "options": {}
  }
}
```

**Webhook URL will be:**
```
https://your-n8n-instance.com/webhook/finscroll
```

---

### 🤖 Step 3: Add OpenAI Agent Node

**Node:** `OpenAI Chat Model` (or AI Agent node)

**Configuration:**

**Model:** `gpt-4-turbo-preview` or `gpt-4`

**System Prompt:**
```
You are FinScroll AI - a financial literacy expert specializing in teaching young Indian adults (18-30 years) about personal finance, stock market, investments, savings, and financial planning.

Your mission is to generate engaging, educational Q&A cards that make complex financial concepts simple and actionable.

CONTEXT:
- Target Audience: Young Indian adults with basic to intermediate financial knowledge
- Use Case: Mobile app with swipe-based learning (TikTok-style for finance)
- Goal: Help users build strong financial fundamentals through bite-sized learning

OUTPUT REQUIREMENTS:
1. Generate exactly {{$json["count"]}} question-answer pairs
2. Topic focus: {{$json["topic"]}}
3. Difficulty mix: 30% easy, 50% medium, 20% advanced
4. Use Indian context: ₹ (INR), NSE, BSE, SEBI, RBI, SBI, HDFC, ICICI, etc.
5. Keep answers concise: 2-3 sentences maximum
6. Use simple language - avoid jargon or explain technical terms
7. Include practical examples from daily life

FINANCIAL THEMES TO COVER:
- Savings & Budgeting (emergency fund, 50-30-20 rule, envelope method)
- Investments (stocks, mutual funds, SIP, diversification, risk management)
- Insurance (term, life, health insurance basics)
- Digital Finance (UPI safety, online banking security, fraud prevention)
- Retirement Planning (PPF, NPS, EPF, retirement corpus calculation)
- Consumer Rights (banking ombudsman, SEBI complaints, grievance redressal)
- Stock Market (NSE, BSE, IPO, trading vs investing, market indices)
- Mutual Funds (SIP, NAV, expense ratio, fund types)

OUTPUT FORMAT (STRICT JSON):
{
  "questions": [
    {
      "id": "unique-uuid-here",
      "question": "What is SIP in mutual funds?",
      "answer": "SIP (Systematic Investment Plan) is like a recurring deposit for mutual funds. You invest a fixed amount (₹500, ₹1000) every month automatically. This helps you build wealth through rupee cost averaging without timing the market.",
      "topic": "Mutual Funds",
      "category": "investments",
      "difficulty": "easy",
      "tags": ["sip", "mutual funds", "investments", "rupee cost averaging"],
      "source": "AI Generated",
      "relatedTopics": ["Mutual Funds 101", "Investment Basics", "SIP vs Lumpsum"]
    }
  ],
  "source": "AI Generated",
  "topic": "{{$json["topic"]}}",
  "totalQuestions": {{$json["count"]}},
  "hasMore": false,
  "nextPage": null
}

EXAMPLE QUESTIONS BY CATEGORY:

SAVINGS:
Q: What is the 50-30-20 budgeting rule?
A: It's a simple budgeting formula: 50% of income for needs (rent, food), 30% for wants (entertainment), and 20% for savings and debt repayment. For ₹30,000 salary, that's ₹15k needs, ₹9k wants, ₹6k savings.

INVESTMENTS:
Q: What's the difference between stocks and mutual funds?
A: Stocks are shares of ONE company (like buying Reliance or TCS). Mutual funds are baskets of MANY stocks managed by experts. Stocks are higher risk but higher potential return. Mutual funds offer diversification and lower risk.

INSURANCE:
Q: What is term insurance?
A: Term insurance is pure life coverage without investment. You pay ₹500-1000/month and your family gets ₹50 lakhs-1 crore if you die. It's the cheapest and most important insurance for earning members.

DIGITAL FINANCE:
Q: How can I avoid UPI scams?
A: Never share UPI PIN or OTP with anyone. UPI doesn't charge money to receive payments. Verify merchant QR codes before scanning. Beware of "KYC update" or "refund" scam calls. Use screen lock on phone.

RETIREMENT:
Q: What is PPF?
A: Public Provident Fund is a 15-year government savings scheme with 7-8% interest. Minimum ₹500, maximum ₹1.5 lakh per year. Interest is tax-free under Section 80C. Best for safe, long-term wealth building.

CONSUMER RIGHTS:
Q: How do I complain about bank issues?
A: Step 1: Complaint to bank's grievance cell. Step 2: Wait 30 days. Step 3: If unresolved, escalate to Banking Ombudsman (RBI). You can file online at RBI's CMS portal. It's free and usually resolved in 60 days.

STOCK MARKET:
Q: What is Nifty 50?
A: Nifty 50 is an index of India's top 50 companies listed on NSE. It represents overall market health. When Nifty goes up, it means most big companies' stocks are rising. Think of it as a temperature gauge for the market.

IMPORTANT:
- Use real Indian company names (Reliance, TCS, HDFC, SBI)
- Reference real institutions (SEBI, RBI, NSE, BSE)
- Use rupee amounts that feel realistic (₹500, ₹1000, ₹5000)
- Mention practical apps (Google Pay, PhonePe, Groww, Zerodha)
- Keep tone friendly and encouraging, not preachy
- Avoid fear-mongering - focus on empowerment

Now generate {{$json["count"]}} questions for: {{$json["topic"]}}
```

**User Prompt (Dynamic):**
```
Generate {{$json["body"]["count"]}} financial literacy questions about: {{$json["body"]["topic"]}}

Category filter: {{$json["body"]["category"]}}
Difficulty filter: {{$json["body"]["difficulty"]}}

Ensure output is valid JSON matching the schema in system prompt.
```

**Configuration JSON:**
```json
{
  "model": "gpt-4-turbo-preview",
  "messages": {
    "values": [
      {
        "role": "system",
        "content": "[PASTE SYSTEM PROMPT ABOVE]"
      },
      {
        "role": "user",
        "content": "Generate {{$json[\"body\"][\"count\"]}} financial literacy questions about: {{$json[\"body\"][\"topic\"]}}"
      }
    ]
  },
  "options": {
    "temperature": 0.7,
    "maxTokens": 4000,
    "topP": 1,
    "frequencyPenalty": 0.2,
    "presencePenalty": 0.3
  },
  "jsonOutput": true
}
```

---

### 📤 Step 4: Add Response Node

**Node:** `Respond to Webhook`

**Configuration:**
```json
{
  "name": "Return Response",
  "type": "n8n-nodes-base.respondToWebhook",
  "position": [650, 300],
  "parameters": {
    "respondWith": "json",
    "responseBody": "={{$json}}",
    "options": {
      "responseHeaders": {
        "entries": [
          {
            "name": "Content-Type",
            "value": "application/json"
          },
          {
            "name": "Access-Control-Allow-Origin",
            "value": "*"
          }
        ]
      }
    }
  }
}
```

---

### 🔗 Step 5: Connect Nodes

**Workflow:**
```
[Webhook Trigger] → [OpenAI Agent] → [Respond to Webhook]
```

---

### ✅ Step 6: Activate Workflow

1. Click **Save** in n8n
2. Toggle workflow to **Active**
3. Copy the webhook URL
4. Update Android app's `NetworkModule.kt` with your webhook URL

---

## 🧪 TESTING THE WORKFLOW

### Test Request (Using Postman/curl):

```bash
curl -X POST https://your-n8n-instance.com/webhook/finscroll \
  -H "Content-Type: application/json" \
  -d '{
    "topic": "Stock Market Basics",
    "count": 5,
    "category": "stock_market",
    "difficulty": "easy"
  }'
```

### Expected Response:

```json
{
  "questions": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "question": "What is the stock market?",
      "answer": "The stock market is a place where people buy and sell company shares. When you buy a stock like Reliance or TCS, you become a small owner of that company. If the company does well, your investment grows.",
      "topic": "Stock Market Basics",
      "category": "stock_market",
      "difficulty": "easy",
      "tags": ["stock market", "basics", "investing", "shares"],
      "source": "AI Generated",
      "relatedTopics": ["Trading vs Investing", "How to Buy Stocks", "NSE vs BSE"]
    },
    {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "question": "What is NSE?",
      "answer": "NSE (National Stock Exchange) is India's largest stock exchange located in Mumbai. Companies list their shares here for trading. The Nifty 50 index tracks NSE's top 50 companies.",
      "topic": "Stock Market Basics",
      "category": "stock_market",
      "difficulty": "easy",
      "tags": ["nse", "stock exchange", "nifty", "india"],
      "source": "AI Generated",
      "relatedTopics": ["BSE vs NSE", "Nifty 50", "Stock Exchanges"]
    }
  ],
  "source": "AI Generated",
  "topic": "Stock Market Basics",
  "totalQuestions": 5,
  "hasMore": false,
  "nextPage": null
}
```

---

## 🎯 ALTERNATIVE: Daily Cards Endpoint

### Add Second Webhook for Daily Mix

**Webhook Path:** `/webhook/finscroll/daily`

**AI Prompt Modification:**
```
Generate 10 random financial literacy questions covering ALL categories:
- 2 questions on Savings & Budgeting
- 2 questions on Investments  
- 2 questions on Insurance
- 1 question on Digital Finance
- 1 question on Retirement
- 1 question on Consumer Rights
- 1 question on Stock Market

Mix difficulty levels: 3 easy, 5 medium, 2 advanced
```

---

## 📱 UPDATE ANDROID APP

**File:** `NetworkModule.kt`

```kotlin
// Replace this line:
private const val BASE_URL = "https://n8n.backend.lehana.in/"

// With your actual n8n URL:
private const val BASE_URL = "https://your-n8n-instance.com/"
```

---

## 🔐 SECURITY CONSIDERATIONS

### Add API Key Authentication (Optional)

**In n8n Webhook Node:**
```json
{
  "options": {
    "authenticationMethod": "headerAuth",
    "headerName": "X-API-Key",
    "headerValue": "your-secret-api-key-here"
  }
}
```

**In Android App (NetworkModule.kt):**
```kotlin
@Provides
@Singleton
fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-API-Key", "your-secret-api-key-here")
                .build()
            chain.proceed(request)
        }
        .build()
}
```

---

## 📊 SAMPLE FINANCIAL TOPICS LIST

```kotlin
// Add to app/src/main/java/com/finscroll/app/data/Constants.kt

object FinancialTopics {
    val ALL_TOPICS = listOf(
        // Basics
        "Financial Literacy 101",
        "Money Management Basics",
        "Personal Finance Fundamentals",
        
        // Savings & Budgeting
        "Emergency Fund Planning",
        "50-30-20 Budgeting Rule",
        "Savings Account vs FD",
        "How to Save Money",
        "Budgeting for Beginners",
        
        // Investments
        "Stock Market Basics",
        "Mutual Funds 101",
        "SIP Explained",
        "Equity vs Debt Funds",
        "Diversification Strategy",
        "Index Funds",
        "Blue Chip Stocks",
        "Value Investing",
        "Growth vs Value Stocks",
        
        // Insurance
        "Term Insurance Basics",
        "Health Insurance Guide",
        "Life Insurance vs ULIP",
        "Insurance Planning",
        
        // Digital Finance
        "UPI Safety Tips",
        "Online Banking Security",
        "Credit Card Safety",
        "Avoiding Financial Scams",
        "Digital Payment Methods",
        
        // Retirement
        "Retirement Planning 101",
        "PPF Benefits",
        "NPS Explained",
        "EPF Guide",
        "Retirement Corpus Calculation",
        
        // Stock Market
        "NSE vs BSE",
        "Trading vs Investing",
        "IPO Basics",
        "Market Indices",
        "Stock Market Indicators",
        "Demat Account Guide",
        
        // Consumer Rights
        "Banking Ombudsman",
        "SEBI Complaints",
        "Consumer Protection",
        "Financial Grievance Redressal",
        
        // Advanced
        "Tax Planning",
        "Capital Gains Tax",
        "Portfolio Rebalancing",
        "Risk Management"
    )
}
```

---

**[END OF PART 3]**

---

# ✅ ALL FILES NOW COMPLETE!

## 📝 SUMMARY OF ALL FILES:

### Part 1 - Project Setup:
1. ✅ build.gradle.kts (Project & App)
2. ✅ settings.gradle.kts
3. ✅ AndroidManifest.xml
4. ✅ strings.xml
5. ✅ themes.xml
6. ✅ Color.kt
7. ✅ Type.kt
8. ✅ Theme.kt

### Part 2 - Data & Logic:
9. ✅ FinancialCard.kt (Data Models)
10. ✅ FinScrollApiService.kt
11. ✅ NetworkModule.kt (Hilt DI)
12. ✅ CardRepository.kt
13. ✅ CardViewModel.kt
14. ✅ MainActivity.kt
15. ✅ FinScrollApp.kt
16. ✅ Navigation.kt
17. ✅ HomeScreen.kt
18. ✅ FinancialCardView.kt

### Part 3 - Remaining UI & Backend:
19. ✅ SearchScreen.kt
20. ✅ ProgressScreen.kt
21. ✅ N8N Complete Workflow Setup
22. ✅ AI Agent Prompts
23. ✅ Testing Guide

---

# 🚀 FINAL DEPLOYMENT STEPS:

1. **Copy all code files** to your Android Studio project
2. **Sync Gradle** - wait for dependencies to download
3. **Set up n8n workflow** using the guide above
4. **Update NetworkModule.kt** with your n8n webhook URL
5. **Build APK** - Build → Build Bundle(s) / APK(s) → Build APK(s)
6. **Test on device/emulator**
7. **Submit to hackathon** 🎉

---

# 🎯 HACKATHON CHECKLIST:

- ✅ No authentication required
- ✅ Infinite scroll interface
- ✅ 3+ financial themes covered (we have 8!)
- ✅ Gamified learning (streaks, levels, progress)
- ✅ Swipe gestures (TikTok-style)
- ✅ Indian context (₹, NSE, BSE, SEBI, etc.)
- ✅ Offline support (caching)
- ✅ Progress tracking
- ✅ Bookmarking feature
- ✅ Search functionality
- ✅ Simple, clean UI

---

**You now have a COMPLETE, production-ready financial literacy app!** 🎊

Reply with "build" if you need help setting up Android Studio or have any questions!
