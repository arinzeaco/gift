package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.PaymentTokenRepository
import com.globasure.giftoga.network.request.ChargeGiftCardRequest
import com.globasure.giftoga.network.response.ChargeWalletResponse
import javax.inject.Inject

class ChargeGiftCardUseCase @Inject constructor(private val paymentTokenRepository: PaymentTokenRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: ChargeWalletResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: ChargeGiftCardRequest): Result {
        return try {
            Result.Success(paymentTokenRepository.chargeGiftCard(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}