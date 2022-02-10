package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.WalletRepository
import com.globasure.giftoga.network.response.ListCardResponse
import javax.inject.Inject

class ListCardUseCase @Inject constructor(private val walletRepository: WalletRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: ListCardResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(order_by: String, page: Int, per_page: Int): Result {
        return try {
            Result.Success(walletRepository.getListBankCards(order_by, page, per_page))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}