package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.GiftCardRepository
import com.globasure.giftoga.network.request.MerchantsRequest
import com.globasure.giftoga.network.response.MerchantsResponse
import javax.inject.Inject

class GetAllMerchantsUseCase @Inject constructor(private val giftCardRepository: GiftCardRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: MerchantsResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: MerchantsRequest): Result {
        return try {
            Result.Success(giftCardRepository.getAllMerchants(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}