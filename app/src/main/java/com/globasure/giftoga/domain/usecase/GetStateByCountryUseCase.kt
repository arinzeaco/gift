package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.AuthRepository
import com.globasure.giftoga.network.response.StateListResponse
import javax.inject.Inject

class GetStateByCountryUseCase @Inject constructor(private val authRepository: AuthRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: StateListResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(country: String): Result {
        return try {
            Result.Success(authRepository.getStatesByCountry(country))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}