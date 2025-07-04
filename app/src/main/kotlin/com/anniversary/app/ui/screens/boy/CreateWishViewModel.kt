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

data class CreateWishUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String = ""
)

@HiltViewModel
class CreateWishViewModel @Inject constructor(
    private val repository: AnniversaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateWishUiState())
    val uiState: StateFlow<CreateWishUiState> = _uiState.asStateFlow()

    fun createWish(wish: WishMessage) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "")

            try {
                val result = repository.saveWishMessage(wish)
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to create message"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unknown error occurred"
                )
            }
        }
    }
}