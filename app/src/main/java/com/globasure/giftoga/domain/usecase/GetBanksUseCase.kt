package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.TransferRepository
import com.globasure.giftoga.network.response.BankListResponse
import javax.inject.Inject

class GetBanksUseCase @Inject constructor(private val transferRepository: TransferRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: BankListResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(): Result {
        return try {
            Result.Success(transferRepository.getBanks())
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}