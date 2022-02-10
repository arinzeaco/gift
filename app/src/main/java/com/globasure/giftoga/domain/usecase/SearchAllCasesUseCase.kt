package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.DisputeRepository
import com.globasure.giftoga.network.request.SearchAllCasesRequest
import com.globasure.giftoga.network.response.AllCasesResponse
import javax.inject.Inject

class SearchAllCasesUseCase @Inject constructor(private val disputeRepository: DisputeRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: AllCasesResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: SearchAllCasesRequest): Result {
        return try {
            Result.Success(disputeRepository.searchAllCases(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}