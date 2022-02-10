package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.GiftCardRepository
import com.globasure.giftoga.network.response.CategoriesResponse
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(private val giftCardRepository: GiftCardRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: CategoriesResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(): Result {
        return try {
            Result.Success(giftCardRepository.getAllCategories())
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}