package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.AuthRepository
import com.globasure.giftoga.network.request.ResetPasswordRequest
import com.globasure.giftoga.network.request.ValidateTokenRequest
import com.globasure.giftoga.network.response.ResetTokenResponse
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(private val authRepository: AuthRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: ResetTokenResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: ResetPasswordRequest): Result {
        return try {
            val validateRequest = ValidateTokenRequest(request.email, request.token)
            val result = authRepository.validateToken(validateRequest)
            if (result.data.token.isNotEmpty()) {
                Result.Success(authRepository.resetPassword(request))
            } else {
                Result.Failure(Exception("Unable to validate token"))
            }
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}