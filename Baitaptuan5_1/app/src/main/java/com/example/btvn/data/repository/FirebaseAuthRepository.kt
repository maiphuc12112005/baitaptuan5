package com.example.btvn.data.repository

import com.example.btvn.model.AuthResponse
import com.example.btvn.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : IAuthRepository {

    /**
     * Đăng nhập bằng Google thông qua ID Token.
     * Trả về AuthResponse.Success nếu thành công, ngược lại là AuthResponse.Error.
     */
    override suspend fun signInWithGoogle(idToken: String): AuthResponse {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                // Tạo đối tượng User từ thông tin Firebase
                val user = User(
                    uid = firebaseUser.uid,
                    name = firebaseUser.displayName ?: "",
                    email = firebaseUser.email ?: ""
                )
                AuthResponse.Success(user)
            } else {
                AuthResponse.Error("Login failed: User not found after authentication.")
            }
        } catch (e: Exception) {
            AuthResponse.Error(e.localizedMessage ?: "Unknown error during Google sign-in")
        }
    }

    /**
     * Lấy người dùng hiện tại nếu đã đăng nhập.
     */
    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    /**
     * Đăng xuất người dùng khỏi Firebase.
     */
    override fun signOut() {
        firebaseAuth.signOut()
    }

    /**
     * Lắng nghe thay đổi trạng thái xác thực của FirebaseAuth.
     */
    override fun getAuthStateChanges(): Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        firebaseAuth.addAuthStateListener(authStateListener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }
}
