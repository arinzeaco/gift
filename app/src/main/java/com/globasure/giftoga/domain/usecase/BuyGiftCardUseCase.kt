package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.GiftCardRepository
import com.globasure.giftoga.network.request.BuyGiftcardRequest
import com.globasure.giftoga.network.response.BuyGiftCardResponse
import javax.inject.Inject

class BuyGiftCardUseCase @Inject constructor(private val giftCardRepository: GiftCardRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: BuyGiftCardResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: BuyGiftcardRequest): Result {
        return try {
            Result.Success(giftCardRepository.buyGiftCard(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}