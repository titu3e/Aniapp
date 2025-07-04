package com.anniversary.app.ui.screens.girl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anniversary.app.data.model.DeliveryStatus
import com.anniversary.app.data.model.WishMessage
import com.anniversary.app.data.repository.AnniversaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class WishDisplayUiState(
    val isLoading: Boolean = false,
    val wishes: List<WishMessage> = emptyList(),
    val errorMessage: String = ""
)

@HiltViewModel
class WishDisplayViewModel @Inject constructor(
    private val repository: AnniversaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WishDisplayUiState())
    val uiState: StateFlow<WishDisplayUiState> = _uiState.asStateFlow()

    fun loadWishes(anniversaryId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // Use flow to get real-time updates
                repository.observeWishesForAnniversary(anniversaryId)
                    .collect { wishes ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            wishes = wishes.filter { it.isDelivered || shouldShowWish(it) }
                        )
                    }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load messages"
                )
            }
        }
    }

    private fun shouldShowWish(wish: WishMessage): Boolean {
        // Logic to determine if wish should be shown based on date
        // For MVP, we'll show all wishes that are created
        return true
    }

    fun addReaction(wishId: String, reaction: String) {
        viewModelScope.launch {
            try {
                val deliveryStatus = DeliveryStatus(
                    wishId = wishId,
                    isRead = true,
                    readAt = Date(),
                    reaction = reaction
                )
                repository.saveDeliveryStatus(deliveryStatus)
            } catch (e: Exception) {
                // Handle error silently or show toast
            }
        }
    }
}