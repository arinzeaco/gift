package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.AuthRepository
import com.globasure.giftoga.network.response.ProfileResponse
import javax.inject.Inject

class GetUserDetailsUseCase @Inject constructor(private val authRepository: AuthRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: ProfileResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(): Result {
        return try {
            Result.Success(authRepository.getAccountDetails())
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}