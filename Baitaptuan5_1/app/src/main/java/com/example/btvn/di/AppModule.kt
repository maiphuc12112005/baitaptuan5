package com.example.btvn.di

import com.example.btvn.data.repository.FirebaseAuthRepository
import com.example.btvn.data.repository.FirebaseUserRepository
import com.example.btvn.data.repository.IAuthRepository
import com.example.btvn.data.repository.IUserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * AppModule cấu hình các dependency (phụ thuộc) cấp toàn cục (Singleton)
 * sử dụng Hilt Dependency Injection.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Cung cấp thể hiện duy nhất của FirebaseAuth.
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()

    /**
     * Cung cấp thể hiện duy nhất của FirebaseFirestore.
     */
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore =
        FirebaseFirestore.getInstance()

    /**
     * Cung cấp implementation của IAuthRepository bằng FirebaseAuthRepository.
     */
    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth
    ): IAuthRepository =
        FirebaseAuthRepository(firebaseAuth)

    /**
     * Cung cấp implementation của IUserRepository bằng FirebaseUserRepository.
     */
    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore
    ): IUserRepository =
        FirebaseUserRepository(firestore)
}
