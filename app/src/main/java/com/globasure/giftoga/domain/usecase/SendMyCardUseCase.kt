package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.GiftCardRepository
import com.globasure.giftoga.network.request.SendGiftCardRequest
import com.globasure.giftoga.network.response.SendGiftCardResponse
import javax.inject.Inject

class SendMyCardUseCase @Inject constructor(private val giftCardRepository: GiftCardRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: SendGiftCardResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: SendGiftCardRequest): Result {
        return try {
            Result.Success(giftCardRepository.sendMyGiftCard(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}