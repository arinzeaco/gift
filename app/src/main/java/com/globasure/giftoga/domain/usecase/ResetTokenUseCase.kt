package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.AuthRepository
import com.globasure.giftoga.network.response.ResetTokenResponse
import javax.inject.Inject

class ResetTokenUseCase @Inject constructor(private val authRepository: AuthRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: ResetTokenResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(email: String): Result {
        return try {
            Result.Success(authRepository.requestResetToken(email))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}