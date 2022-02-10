package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.PaymentTokenRepository
import com.globasure.giftoga.network.request.ChargeWalletRequest
import com.globasure.giftoga.network.request.GetLinkForSalesRequest
import com.globasure.giftoga.network.response.ChargeWalletResponse
import javax.inject.Inject

class ChargeWalletUseCase @Inject constructor(private val paymentTokenRepository: PaymentTokenRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: ChargeWalletResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(Request: GetLinkForSalesRequest, chargeWalletRequest: ChargeWalletRequest): Result {
        return try {
            val payLink = paymentTokenRepository.getPayLinkForSales(Request).data.payNumber
            chargeWalletRequest.payLink = payLink
            Result.Success(paymentTokenRepository.chargeWallet(chargeWalletRequest))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}