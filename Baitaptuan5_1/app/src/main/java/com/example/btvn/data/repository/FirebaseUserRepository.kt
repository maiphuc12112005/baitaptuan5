package com.example.btvn.data.repository

import com.example.btvn.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseUserRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : IUserRepository {

    private val usersCollection = firestore.collection("users")

    /**
     * Lấy thông tin hồ sơ người dùng từ Firestore theo userId.
     * Trả về luồng Flow<User?> để theo dõi thay đổi theo thời gian thực.
     */
    override fun getUserProfile(userId: String): Flow<User?> = callbackFlow {
        val listenerRegistration = usersCollection.document(userId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    trySend(null) // Gửi null nếu có lỗi
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    trySend(snapshot.toObject(User::class.java))
                } else {
                    trySend(null)
                }
            }

        awaitClose { listenerRegistration.remove() }
    }

    /**
     * Lưu hoặc cập nhật hồ sơ người dùng vào Firestore.
     * Trả về true nếu thành công, false nếu có lỗi.
     */
    override suspend fun saveUserProfile(user: User): Boolean {
        return try {
            usersCollection.document(user.uid).set(user).await()
            true
        } catch (e: Exception) {
            // Có thể log lỗi tại đây nếu cần
            false
        }
    }
}
