package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.WalletRepository
import com.globasure.giftoga.network.response.RemoveCardResponse
import javax.inject.Inject

class DeleteCardUseCase @Inject constructor(private val walletRepository: WalletRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: RemoveCardResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(card_token: String): Result {
        return try {
            Result.Success(walletRepository.deleteBankCard(card_token))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}