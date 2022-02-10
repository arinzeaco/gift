package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.GiftCardRepository
import com.globasure.giftoga.network.response.CategoryResponse
import javax.inject.Inject

class GetCategoryDetailUseCase @Inject constructor(private val giftCardRepository: GiftCardRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: CategoryResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(category_id: String): Result {
        return try {
            Result.Success(giftCardRepository.getCategoryDetail(category_id))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}