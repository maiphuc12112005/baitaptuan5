package com.example.btvn.model

/**
 * Data class đại diện cho thông tin người dùng trong ứng dụng.
 *
 * @property uid Mã định danh duy nhất của người dùng (Firebase UID).
 * @property name Tên đầy đủ của người dùng.
 * @property email Địa chỉ email đã xác thực.
 * @property dateOfBirth Ngày sinh của người dùng (định dạng "DD/MM/YYYY").
 */
data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val dateOfBirth: String = ""
    // Có thể mở rộng thêm các trường như: avatarUrl, phoneNumber, address,...
)
