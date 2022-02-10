package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.TransactionRepository
import com.globasure.giftoga.network.request.TransactionRequest
import com.globasure.giftoga.network.response.TransactionResponse
import javax.inject.Inject

class GetAllTransactionUseCase @Inject constructor(private val transactionRepository: TransactionRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: TransactionResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: TransactionRequest): Result {
        return try {
            Result.Success(transactionRepository.getAllTransaction(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}