package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.GiftCardRepository
import com.globasure.giftoga.network.response.RedeemGiftCardResponse
import javax.inject.Inject

class RedeemCardUseCase @Inject constructor(private val giftCardRepository: GiftCardRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: RedeemGiftCardResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(giftcard: String, amount: String): Result {
        return try {
            Result.Success(giftCardRepository.redeemGiftCard(giftcard, amount))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}