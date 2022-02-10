package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.PaymentTokenRepository
import com.globasure.giftoga.network.request.ChargeFeesRequest
import com.globasure.giftoga.network.response.ChargeFeesResponse
import javax.inject.Inject

class ChargeFeesUseCase @Inject constructor(private val paymentTokenRepository: PaymentTokenRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: ChargeFeesResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: ChargeFeesRequest): Result {
        return try {

            Result.Success(paymentTokenRepository.chargeFees(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}