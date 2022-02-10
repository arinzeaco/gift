package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.TransferRepository
import com.globasure.giftoga.network.request.TransferCommissionRequest
import com.globasure.giftoga.network.response.TransferCommissionResponse
import javax.inject.Inject

class TransferCommissionUseCase @Inject constructor(private val transferRepository: TransferRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: TransferCommissionResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: TransferCommissionRequest): Result {
        return try {
            Result.Success(transferRepository.transferCommission(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}