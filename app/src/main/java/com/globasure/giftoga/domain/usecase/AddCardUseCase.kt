package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.WalletRepository
import com.globasure.giftoga.network.response.AddCardResponse
import javax.inject.Inject

class AddCardUseCase @Inject constructor(
    private val walletRepository: WalletRepository
) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: AddCardResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(paymentToken: String): Result {
        return try {
            Result.Success(walletRepository.addBankCard(paymentToken))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}