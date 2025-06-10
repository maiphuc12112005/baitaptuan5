package com.example.btvn.model

/**
 * AuthResponse là sealed class biểu diễn các trạng thái khác nhau
 * trong quá trình xác thực người dùng (authentication).
 */
sealed class AuthResponse {

    /**
     * Trạng thái đang tải dữ liệu (ví dụ: đang đăng nhập).
     */
    object Loading : AuthResponse()

    /**
     * Trạng thái thành công, có thể chứa dữ liệu kết quả.
     * @param data Dữ liệu trả về, có thể null.
     */
    data class Success(val data: Any? = null) : AuthResponse()

    /**
     * Trạng thái lỗi, chứa thông báo lỗi.
     * @param errorMessage Mô tả lỗi rõ ràng.
     */
    data class Error(val errorMessage: String) : AuthResponse()

    /**
     * Trạng thái mặc định hoặc không có hành động.
     */
    object Idle : AuthResponse()
}
