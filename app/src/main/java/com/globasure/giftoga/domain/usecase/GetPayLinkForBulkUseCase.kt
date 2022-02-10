package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.PaymentTokenRepository
import com.globasure.giftoga.network.request.GetLinkForBulkRequest
import com.globasure.giftoga.network.response.PayLinkResponse
import javax.inject.Inject

class GetPayLinkForBulkUseCase @Inject constructor(private val paymentTokenRepository: PaymentTokenRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: PayLinkResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: GetLinkForBulkRequest): Result {
        return try {
            Result.Success(paymentTokenRepository.getPayLinkForBulk(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}