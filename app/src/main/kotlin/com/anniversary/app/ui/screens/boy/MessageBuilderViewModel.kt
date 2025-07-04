package com.anniversary.app.ui.screens.boy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anniversary.app.data.model.WishMessage
import com.anniversary.app.data.repository.AnniversaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MessageBuilderUiState(
    val isLoading: Boolean = false,
    val wishes: List<WishMessage> = emptyList(),
    val errorMessage: String = ""
)

@HiltViewModel
class MessageBuilderViewModel @Inject constructor(
    private val repository: AnniversaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageBuilderUiState())
    val uiState: StateFlow<MessageBuilderUiState> = _uiState.asStateFlow()

    fun loadWishes(anniversaryId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val result = repository.getWishesForAnniversary(anniversaryId)
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        wishes = result.getOrNull() ?: emptyList()
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to load wishes"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unknown error"
                )
            }
        }
    }
}