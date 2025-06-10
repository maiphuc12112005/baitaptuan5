package com.example.btvn.data.repository

import com.example.btvn.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Giao diện định nghĩa các hành động thao tác với hồ sơ người dùng (User Profile)
 * thông qua Firebase Firestore hoặc bất kỳ nguồn dữ liệu nào khác.
 */
interface IUserRepository {

    /**
     * Lấy thông tin hồ sơ của người dùng theo userId.
     *
     * @param userId ID duy nhất của người dùng.
     * @return Flow phát ra đối tượng User nếu tìm thấy, hoặc null nếu không có.
     */
    fun getUserProfile(userId: String): Flow<User?>

    /**
     * Lưu hoặc cập nhật thông tin hồ sơ người dùng vào cơ sở dữ liệu.
     *
     * @param user Đối tượng User chứa dữ liệu hồ sơ cần lưu.
     * @return true nếu lưu thành công, false nếu có lỗi xảy ra.
     */
    suspend fun saveUserProfile(user: User): Boolean
}
