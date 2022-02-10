package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.PaymentTokenRepository
import com.globasure.giftoga.network.request.CardOtpRequest
import com.globasure.giftoga.network.response.CardOtpResponse
import javax.inject.Inject

class CardOtpUseCase @Inject constructor(private val paymentTokenRepository: PaymentTokenRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: CardOtpResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: CardOtpRequest): Result {
        return try {
            Result.Success(paymentTokenRepository.cardOtp(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}