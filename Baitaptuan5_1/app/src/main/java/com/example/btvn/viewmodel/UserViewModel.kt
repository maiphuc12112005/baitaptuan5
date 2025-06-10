package com.example.btvn.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btvn.data.repository.IUserRepository
import com.example.btvn.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: IUserRepository
) : ViewModel() {

    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> = _userProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _updateSuccess = MutableStateFlow<Boolean?>(null)
    val updateSuccess: StateFlow<Boolean?> = _updateSuccess.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /**
     * Tải dữ liệu hồ sơ người dùng từ repository theo userId.
     */
    fun loadUserProfile(userId: String) {
        Log.d("UserViewModel", "Loading user profile for UID: $userId")
        viewModelScope.launch {
            userRepository.getUserProfile(userId)
                .onStart {
                    _isLoading.value = true
                    _error.value = null
                }
                .catch { e ->
                    _error.value = "Failed to load user profile: ${e.localizedMessage}"
                    _isLoading.value = false
                    Log.e("UserViewModel", "Load failed: ${e.localizedMessage}", e)
                }
                .collect { user ->
                    _userProfile.value = user
                    _isLoading.value = false
                    Log.d("UserViewModel", "User profile loaded: ${user?.email ?: "null"}")
                }
        }
    }

    /**
     * Cập nhật hồ sơ người dùng và lưu lại vào repository.
     */
    fun updateUserProfile(user: User) {
        Log.d("UserViewModel", "Updating profile for UID: ${user.uid}")
        _isLoading.value = true
        _updateSuccess.value = null
        _error.value = null

        viewModelScope.launch {
            try {
                val success = userRepository.saveUserProfile(user)
                _updateSuccess.value = success
                _isLoading.value = false

                if (success) {
                    _userProfile.value = user
                    Log.d("UserViewModel", "Update success for ${user.email}")
                } else {
                    _error.value = "Failed to save user profile in repository."
                    Log.e("UserViewModel", "Save returned false for ${user.email}")
                }
            } catch (e: Exception) {
                _updateSuccess.value = false
                _isLoading.value = false
                _error.value = "Failed to update profile: ${e.localizedMessage}"
                Log.e("UserViewModel", "Update failed: ${e.localizedMessage}", e)
            }
        }
    }

    /**
     * Đặt lại trạng thái lỗi về null sau khi đã hiển thị cho người dùng.
     */
    fun resetErrorState() {
        _error.value = null
    }

    /**
     * Đặt lại trạng thái thành công sau khi xử lý.
     */
    fun resetUpdateSuccessState() {
        _updateSuccess.value = null
    }
}
