package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.TransactionRepository
import com.globasure.giftoga.network.request.TransactionRequest
import com.globasure.giftoga.network.response.ExportTransactionResponse
import javax.inject.Inject

class ExportTransactionUseCase @Inject constructor(private val transactionRepository: TransactionRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: ExportTransactionResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: TransactionRequest): Result {
        return try {
            Result.Success(transactionRepository.exportTransaction(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}