package com.anniversary.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.anniversary.app.data.model.WishMessage
import com.anniversary.app.ui.screens.boy.CreateWishViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWishDialog(
    anniversaryId: String,
    monthNumber: Int,
    onDismiss: () -> Unit,
    onWishCreated: () -> Unit,
    viewModel: CreateWishViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var hasConfetti by remember { mutableStateOf(true) }
    var selectedColor by remember { mutableStateOf("#FF69B4") }
    
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onWishCreated()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Create Anniversary Message",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Month number
                Text(
                    text = "Month $monthNumber",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFFF1493)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Title input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Message input
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Anniversary Message *") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 6
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Color selection
                Text(
                    text = "Background Color",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val colors = listOf(
                        "#FF69B4" to "Hot Pink",
                        "#FF1493" to "Deep Pink", 
                        "#DC143C" to "Crimson",
                        "#FF6347" to "Tomato",
                        "#9370DB" to "Purple",
                        "#4169E1" to "Blue"
                    )
                    
                    colors.forEach { (colorCode, colorName) ->
                        val color = Color(android.graphics.Color.parseColor(colorCode))
                        Card(
                            modifier = Modifier
                                .size(40.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = color),
                            border = if (selectedColor == colorCode) 
                                androidx.compose.foundation.BorderStroke(3.dp, Color.Black) 
                                else null,
                            onClick = { selectedColor = colorCode }
                        ) {}
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Confetti toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Add Confetti Animation 🎊",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Switch(
                        checked = hasConfetti,
                        onCheckedChange = { hasConfetti = it }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Preview
                Text(
                    text = "Preview:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(android.graphics.Color.parseColor(selectedColor))
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (title.isNotEmpty()) {
                            Text(
                                text = title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        Text(
                            text = message.ifEmpty { "Your anniversary message will appear here..." },
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = if (message.isEmpty()) 0.7f else 1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                    
                    Button(
                        onClick = {
                            if (message.isNotBlank()) {
                                val wish = WishMessage(
                                    anniversaryId = anniversaryId,
                                    monthNumber = monthNumber,
                                    title = title,
                                    message = message,
                                    hasConfetti = hasConfetti,
                                    backgroundColor = selectedColor,
                                    textColor = "#FFFFFF"
                                )
                                viewModel.createWish(wish)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = message.isNotBlank() && !uiState.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF1493)
                        )
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White
                            )
                        } else {
                            Text("Create Message")
                        }
                    }
                }

                // Error message
                if (uiState.errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}