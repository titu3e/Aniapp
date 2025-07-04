package com.anniversary.app.data.model

import java.util.Date

data class Anniversary(
    val id: String = "",
    val boyId: String = "",
    val girlId: String = "",
    val relationshipStartDate: Date = Date(),
    val coupleCode: String = "",
    val isActive: Boolean = true,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

data class WishMessage(
    val id: String = "",
    val anniversaryId: String = "",
    val monthNumber: Int = 1, // 1 for first month, 2 for second, etc.
    val title: String = "",
    val message: String = "",
    val imageUrl: String? = null,
    val audioUrl: String? = null,
    val hasConfetti: Boolean = true,
    val lottieAnimationUrl: String? = null,
    val backgroundColor: String = "#FF69B4", // Default pink
    val textColor: String = "#FFFFFF",
    val scheduledDate: Date? = null,
    val isDelivered: Boolean = false,
    val deliveredAt: Date? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

data class UserProfile(
    val id: String = "",
    val role: UserRole = UserRole.BOY,
    val name: String = "",
    val email: String? = null,
    val profileImageUrl: String? = null,
    val anniversaryId: String? = null,
    val deviceToken: String? = null,
    val isOnline: Boolean = false,
    val lastSeen: Date = Date(),
    val createdAt: Date = Date()
)

enum class UserRole {
    BOY, GIRL
}

data class DeliveryStatus(
    val wishId: String = "",
    val isRead: Boolean = false,
    val readAt: Date? = null,
    val reaction: String? = null, // emoji reaction
    val comment: String? = null
)