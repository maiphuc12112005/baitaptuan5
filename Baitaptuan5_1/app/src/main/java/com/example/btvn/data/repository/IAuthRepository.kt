package com.example.btvn.data.repository

import com.example.btvn.model.AuthResponse
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

/**
 * Giao diện định nghĩa các hành động xác thực người dùng (authentication)
 * sử dụng Firebase và Google Sign-In.
 */
interface IAuthRepository {

    /**
     * Đăng nhập bằng Google với ID Token lấy từ Google Sign-In.
     *
     * @param idToken Mã ID Token từ tài khoản Google.
     * @return AuthResponse thành công hoặc lỗi tương ứng.
     */
    suspend fun signInWithGoogle(idToken: String): AuthResponse

    /**
     * Lấy người dùng hiện tại nếu đã đăng nhập.
     *
     * @return Đối tượng FirebaseUser nếu đã đăng nhập, ngược lại là null.
     */
    fun getCurrentUser(): FirebaseUser?

    /**
     * Đăng xuất người dùng hiện tại khỏi Firebase.
     */
    fun signOut()

    /**
     * Lắng nghe thay đổi trạng thái xác thực (sign-in/sign-out).
     *
     * @return Flow<FirebaseUser?> phát ra người dùng hiện tại mỗi khi trạng thái thay đổi.
     */
    fun getAuthStateChanges(): Flow<FirebaseUser?>
}
