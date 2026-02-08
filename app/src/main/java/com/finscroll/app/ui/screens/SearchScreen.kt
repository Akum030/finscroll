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
                title = { Text("Search Topics") },
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
                text = "Browse by Category",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val categories = getFilteredCategories(searchQuery)
                items(categories) { category ->
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
        FinancialCategory.DIGITAL_FINANCE -> Icons.Default.AccountBalance
        FinancialCategory.RETIREMENT -> Icons.Default.Elderly
        FinancialCategory.CONSUMER_RIGHTS -> Icons.Default.Gavel
        FinancialCategory.STOCK_MARKET -> Icons.Default.ShowChart
        FinancialCategory.MUTUAL_FUNDS -> Icons.Default.PieChart
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
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .padding(12.dp)
                        .size(32.dp)
                )
            }
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = getCategoryDescription(category),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
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
        FinancialCategory.INVESTMENTS -> "Understand stocks, bonds, and investment strategies"
        FinancialCategory.INSURANCE -> "Learn about life, health, and term insurance"
        FinancialCategory.DIGITAL_FINANCE -> "UPI safety, online banking, and fraud prevention"
        FinancialCategory.RETIREMENT -> "PPF, NPS, EPF, and retirement planning"
        FinancialCategory.CONSUMER_RIGHTS -> "Know your rights, SEBI guidelines, complaints"
        FinancialCategory.STOCK_MARKET -> "NSE, BSE, trading, and stock market basics"
        FinancialCategory.MUTUAL_FUNDS -> "SIP, NAV, fund types, and mutual fund investing"
        else -> "General financial literacy concepts"
    }
}
