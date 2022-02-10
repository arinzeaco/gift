package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.AuthRepository
import com.globasure.giftoga.network.request.ValidateTokenRequest
import com.globasure.giftoga.network.response.ValidateTokenResponse
import javax.inject.Inject

class ValidateTokenUseCase @Inject constructor(private val authRepository: AuthRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: ValidateTokenResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: ValidateTokenRequest): Result {
        return try {
            Result.Success(authRepository.validateToken(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}