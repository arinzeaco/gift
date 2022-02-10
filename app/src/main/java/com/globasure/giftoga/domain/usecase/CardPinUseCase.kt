package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.PaymentTokenRepository
import com.globasure.giftoga.network.request.CardPinRequest
import com.globasure.giftoga.network.response.CardPinResponse
import javax.inject.Inject

class CardPinUseCase @Inject constructor(private val paymentTokenRepository: PaymentTokenRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: CardPinResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: CardPinRequest): Result {
        return try {
            Result.Success(paymentTokenRepository.cardPin(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}