package com.anniversary.app.ui.screens.boy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anniversary.app.data.model.Anniversary
import com.anniversary.app.data.model.UserProfile
import com.anniversary.app.data.model.UserRole
import com.anniversary.app.data.repository.AnniversaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

data class BoySetupUiState(
    val isLoading: Boolean = false,
    val anniversaryId: String = "",
    val coupleCode: String = "",
    val errorMessage: String = ""
)

@HiltViewModel
class BoySetupViewModel @Inject constructor(
    private val repository: AnniversaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BoySetupUiState())
    val uiState: StateFlow<BoySetupUiState> = _uiState.asStateFlow()

    fun createAnniversary(boyName: String, startDate: LocalDate) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "")

            try {
                // Create user profile first
                val boyProfile = UserProfile(
                    role = UserRole.BOY,
                    name = boyName
                )

                val userResult = repository.createUser(boyProfile)
                if (userResult.isFailure) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to create user profile"
                    )
                    return@launch
                }

                val user = userResult.getOrNull()!!

                // Create anniversary
                val anniversary = Anniversary(
                    boyId = user.id,
                    relationshipStartDate = Date.from(
                        startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
                    )
                )

                val anniversaryResult = repository.createAnniversary(anniversary)
                if (anniversaryResult.isSuccess) {
                    val createdAnniversary = anniversaryResult.getOrNull()!!
                    
                    // Update user with anniversary ID
                    repository.updateUserProfile(
                        user.id, 
                        mapOf("anniversaryId" to createdAnniversary.id)
                    )

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        anniversaryId = createdAnniversary.id,
                        coupleCode = createdAnniversary.coupleCode
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to create anniversary"
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