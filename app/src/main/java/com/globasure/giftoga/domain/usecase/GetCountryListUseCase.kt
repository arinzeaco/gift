package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.AuthRepository
import com.globasure.giftoga.network.response.CountryListResponse
import javax.inject.Inject

class GetCountryListUseCase @Inject constructor(private val authRepository: AuthRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: CountryListResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(): Result {
        return try {
            Result.Success(authRepository.getCountryList())
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}