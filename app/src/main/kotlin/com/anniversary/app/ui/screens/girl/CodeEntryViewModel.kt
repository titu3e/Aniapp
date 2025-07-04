package com.anniversary.app.ui.screens.girl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anniversary.app.data.model.UserProfile
import com.anniversary.app.data.model.UserRole
import com.anniversary.app.data.repository.AnniversaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CodeEntryUiState(
    val isLoading: Boolean = false,
    val anniversaryId: String = "",
    val errorMessage: String = ""
)

@HiltViewModel
class CodeEntryViewModel @Inject constructor(
    private val repository: AnniversaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CodeEntryUiState())
    val uiState: StateFlow<CodeEntryUiState> = _uiState.asStateFlow()

    fun verifyCode(code: String, girlName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "")

            try {
                // Find anniversary by code
                val anniversaryResult = repository.findAnniversaryByCode(code)
                if (anniversaryResult.isFailure) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to verify code"
                    )
                    return@launch
                }

                val anniversary = anniversaryResult.getOrNull()
                if (anniversary == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Invalid code. Please check and try again."
                    )
                    return@launch
                }

                // Check if already linked to another girl
                if (anniversary.girlId.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "This code is already in use."
                    )
                    return@launch
                }

                // Create girl profile
                val girlProfile = UserProfile(
                    role = UserRole.GIRL,
                    name = girlName,
                    anniversaryId = anniversary.id
                )

                val userResult = repository.createUser(girlProfile)
                if (userResult.isFailure) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to create profile"
                    )
                    return@launch
                }

                val girl = userResult.getOrNull()!!

                // Link girl to anniversary
                val linkResult = repository.linkGirlToAnniversary(anniversary.id, girl.id)
                if (linkResult.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        anniversaryId = anniversary.id
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to link profiles"
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