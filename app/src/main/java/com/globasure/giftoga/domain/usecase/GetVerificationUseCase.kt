package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.VerificationRepository
import com.globasure.giftoga.network.response.VerificationResponse
import javax.inject.Inject

class GetVerificationUseCase @Inject constructor(private val verificationRepository: VerificationRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: VerificationResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(): Result {
        return try {
            Result.Success(verificationRepository.getVerificationStatus())
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}