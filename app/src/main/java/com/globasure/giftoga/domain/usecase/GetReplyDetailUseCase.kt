package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.DisputeRepository
import com.globasure.giftoga.network.response.ReplyDetailResponse
import javax.inject.Inject

class GetReplyDetailUseCase @Inject constructor(private val disputeRepository: DisputeRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: ReplyDetailResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(case_id: String, comment_id: String): Result {
        return try {
            Result.Success(disputeRepository.getReplyDetail(case_id, comment_id))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}