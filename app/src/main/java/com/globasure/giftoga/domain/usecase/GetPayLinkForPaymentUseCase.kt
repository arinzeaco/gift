package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.PaymentTokenRepository
import com.globasure.giftoga.network.request.GetLinkForPaymentRequest
import com.globasure.giftoga.network.response.PayLinkResponse
import javax.inject.Inject

class GetPayLinkForPaymentUseCase @Inject constructor(private val paymentTokenRepository: PaymentTokenRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: PayLinkResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: GetLinkForPaymentRequest): Result {
        return try {
            Result.Success(paymentTokenRepository.getLinkForPayment(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}