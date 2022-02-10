package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.GiftCardRepository
import com.globasure.giftoga.network.response.MerchantDetailResponse
import javax.inject.Inject

class GetMerchantDetailUseCase @Inject constructor(private val giftCardRepository: GiftCardRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: MerchantDetailResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(merchant_id: String): Result {
        return try {
            Result.Success(giftCardRepository.getMerchantDetail(merchant_id))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}