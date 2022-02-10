package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.DisputeRepository
import com.globasure.giftoga.network.response.AllReplyResponse
import javax.inject.Inject

class GetAllReplyUseCase @Inject constructor(private val disputeRepository: DisputeRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: AllReplyResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(case_id: String): Result {
        return try {
            Result.Success(disputeRepository.getAllReply(case_id))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}