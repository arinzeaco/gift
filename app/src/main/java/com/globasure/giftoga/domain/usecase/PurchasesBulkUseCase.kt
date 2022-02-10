package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.BulkRepository
import com.globasure.giftoga.network.response.PurchaseBulkResponse
import javax.inject.Inject

class PurchasesBulkUseCase @Inject constructor(private val bulkRepository: BulkRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: PurchaseBulkResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(payment_token: String): Result {
        return try {
            Result.Success(bulkRepository.purchasesBulk(payment_token))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}