package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.DisputeRepository
import com.globasure.giftoga.network.request.OpenCaseRequest
import com.globasure.giftoga.network.response.OpenCaseResponse
import javax.inject.Inject

class OpenCaseUseCase @Inject constructor(private val disputeRepository: DisputeRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: OpenCaseResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: OpenCaseRequest): Result {
        return try {
            Result.Success(disputeRepository.openCase(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}