package com.anniversary.app.ui.screens.boy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anniversary.app.ui.components.WishMessageCard
import com.anniversary.app.ui.components.CreateWishDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageBuilderScreen(
    anniversaryId: String,
    onBackClick: () -> Unit,
    viewModel: MessageBuilderViewModel = hiltViewModel()
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(anniversaryId) {
        viewModel.loadWishes(anniversaryId)
    }

    if (showCreateDialog) {
        CreateWishDialog(
            anniversaryId = anniversaryId,
            monthNumber = uiState.wishes.size + 1,
            onDismiss = { showCreateDialog = false },
            onWishCreated = {
                showCreateDialog = false
                viewModel.loadWishes(anniversaryId)
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFF69B4),
                        Color(0xFFFF1493)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Anniversary Messages",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                )
                IconButton(onClick = { showCreateDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Message",
                        tint = Color.White
                    )
                }
            }

            // Content
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (uiState.wishes.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "No messages yet!",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Create your first anniversary message to get started.",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = { showCreateDialog = true },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFFF1493)
                                        )
                                    ) {
                                        Text("Create First Message")
                                    }
                                }
                            }
                        }
                    } else {
                        itemsIndexed(uiState.wishes) { index, wish ->
                            WishMessageCard(
                                wish = wish,
                                onEdit = { 
                                    // TODO: Implement edit functionality
                                },
                                onDelete = { 
                                    // TODO: Implement delete functionality
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}