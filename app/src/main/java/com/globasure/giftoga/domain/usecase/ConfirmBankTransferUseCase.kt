package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.PaymentTokenRepository
import com.globasure.giftoga.network.response.ChargeBankTransferResponse
import javax.inject.Inject

class ConfirmBankTransferUseCase @Inject constructor(private val paymentTokenRepository: PaymentTokenRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: ChargeBankTransferResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(reference: String): Result {
        return try {
            Result.Success(paymentTokenRepository.confirmBankTransfer(reference))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}