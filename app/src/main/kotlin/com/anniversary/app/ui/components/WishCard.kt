package com.anniversary.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anniversary.app.data.model.WishMessage
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WishCard(
    wish: WishMessage,
    onReaction: (String) -> Unit
) {
    var showReactions by remember { mutableStateOf(false) }
    val backgroundColor = try {
        Color(android.graphics.Color.parseColor(wish.backgroundColor))
    } catch (e: Exception) {
        Color(0xFFFF69B4)
    }
    
    val textColor = try {
        Color(android.graphics.Color.parseColor(wish.textColor))
    } catch (e: Exception) {
        Color.White
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showReactions = !showReactions },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            backgroundColor,
                            backgroundColor.copy(alpha = 0.8f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header with month number
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Month ${wish.monthNumber}",
                        fontSize = 14.sp,
                        color = textColor.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = textColor.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title
                if (wish.title.isNotEmpty()) {
                    Text(
                        text = wish.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Message
                Text(
                    text = wish.message,
                    fontSize = 16.sp,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                // Image placeholder (if imageUrl exists)
                if (!wish.imageUrl.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Gray.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "📸 Image",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                // Audio placeholder (if audioUrl exists)
                if (!wish.audioUrl.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = textColor.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "🎵 Audio Message",
                                fontSize = 14.sp,
                                color = textColor,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Delivery info
                if (wish.deliveredAt != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Delivered: ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(wish.deliveredAt)}",
                        fontSize = 12.sp,
                        color = textColor.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Reaction buttons
                if (showReactions) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val reactions = listOf("❤️", "😍", "🥰", "😘", "🤗", "😊")
                        reactions.forEach { reaction ->
                            ElevatedButton(
                                onClick = { 
                                    onReaction(reaction)
                                    showReactions = false
                                },
                                modifier = Modifier.size(40.dp),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = Color.White.copy(alpha = 0.9f)
                                )
                            ) {
                                Text(
                                    text = reaction,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            // Confetti overlay for this card if enabled
            if (wish.hasConfetti && showReactions) {
                ConfettiAnimation(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}