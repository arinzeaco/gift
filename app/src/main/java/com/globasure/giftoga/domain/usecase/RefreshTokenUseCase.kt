package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.AuthRepository
import com.globasure.giftoga.network.response.LoginResponse
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(private val authRepository: AuthRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: LoginResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(refreshToken: String): Result {
        return try {
            Result.Success(authRepository.refreshToken(refreshToken))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}