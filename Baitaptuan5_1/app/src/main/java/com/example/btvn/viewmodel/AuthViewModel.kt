package com.example.btvn.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btvn.data.repository.IAuthRepository
import com.example.btvn.data.repository.IUserRepository
import com.example.btvn.model.AuthResponse
import com.example.btvn.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository
) : ViewModel() {

    private val _authStatus = MutableStateFlow<AuthResponse>(AuthResponse.Idle)
    val authStatus: StateFlow<AuthResponse> = _authStatus.asStateFlow()

    private val _currentUserData = MutableStateFlow<User?>(null)
    val currentUserData: StateFlow<User?> = _currentUserData.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.getAuthStateChanges().collect { firebaseUser ->
                Log.d("AuthViewModel", "Auth state changed: ${firebaseUser?.email ?: "No user"}")

                if (firebaseUser != null) {
                    try {
                        val existingUser = userRepository.getUserProfile(firebaseUser.uid).firstOrNull()

                        if (existingUser != null) {
                            _currentUserData.value = existingUser
                            _authStatus.value = AuthResponse.Success(existingUser)
                            Log.d("AuthViewModel", "User profile loaded: ${existingUser.email}")
                        } else {
                            val newUser = User(
                                uid = firebaseUser.uid,
                                name = firebaseUser.displayName ?: "New User",
                                email = firebaseUser.email ?: "",
                                dateOfBirth = "" // Default DOB
                            )
                            userRepository.saveUserProfile(newUser)
                            _currentUserData.value = newUser
                            _authStatus.value = AuthResponse.Success(newUser)
                            Log.d("AuthViewModel", "New user profile saved: ${newUser.email}")
                        }

                    } catch (e: Exception) {
                        _authStatus.value = AuthResponse.Error("Failed to load/save user profile: ${e.localizedMessage}")
                        Log.e("AuthViewModel", "Error: ${e.localizedMessage}", e)
                    }
                } else {
                    _authStatus.value = AuthResponse.Idle
                    _currentUserData.value = null
                    Log.d("AuthViewModel", "User signed out.")
                }
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        _authStatus.value = AuthResponse.Loading
        Log.d("AuthViewModel", "Signing in with Google...")

        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(idToken)
            if (result is AuthResponse.Error) {
                _authStatus.value = result
                Log.e("AuthViewModel", "Google Sign-In failed: ${result.errorMessage}")
            }
            // Success is handled by authState observer
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                authRepository.signOut()
                Log.d("AuthViewModel", "Sign-out successful.")
            } catch (e: Exception) {
                _authStatus.value = AuthResponse.Error("Sign out failed: ${e.localizedMessage}")
                Log.e("AuthViewModel", "Sign-out error: ${e.localizedMessage}", e)
            }
        }
    }
}
