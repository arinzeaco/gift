package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.DisputeRepository
import com.globasure.giftoga.network.response.ReplyCaseResponse
import javax.inject.Inject

class ReplyCaseUseCase @Inject constructor(private val disputeRepository: DisputeRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: ReplyCaseResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(case_id: String, comment: String): Result {
        return try {
            Result.Success(disputeRepository.replyCase(case_id, comment))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}