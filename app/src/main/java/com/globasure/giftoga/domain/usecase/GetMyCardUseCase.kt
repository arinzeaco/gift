package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.GiftCardRepository
import com.globasure.giftoga.network.request.MyGiftCardRequest
import com.globasure.giftoga.network.response.MyGiftCardResponse
import javax.inject.Inject

class GetMyCardUseCase @Inject constructor(private val giftCardRepository: GiftCardRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: MyGiftCardResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: MyGiftCardRequest): Result {
        return try {
            Result.Success(giftCardRepository.getMyGiftCard(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}