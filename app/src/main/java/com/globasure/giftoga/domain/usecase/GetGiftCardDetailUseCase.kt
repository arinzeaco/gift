package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.GiftCardRepository
import com.globasure.giftoga.network.response.GiftcardDetailResponse
import javax.inject.Inject

class GetGiftCardDetailUseCase @Inject constructor(private val giftCardRepository: GiftCardRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: GiftcardDetailResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(giftCard: String): Result {
        return try {
            Result.Success(giftCardRepository.getGiftCardDetail(giftCard))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}