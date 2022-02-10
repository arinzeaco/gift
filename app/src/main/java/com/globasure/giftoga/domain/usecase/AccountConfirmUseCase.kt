package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.AuthRepository
import com.globasure.giftoga.network.response.SignUpResponse
import javax.inject.Inject

class AccountConfirmUseCase @Inject constructor(private val authRepository: AuthRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: SignUpResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(token: String): Result {
        return try {
            Result.Success(authRepository.requestConfirmAccount(token))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}