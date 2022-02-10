package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.AuthRepository
import com.globasure.giftoga.network.request.SignUpPersonalRequest
import com.globasure.giftoga.network.response.SignUpResponse
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val authRepository: AuthRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: SignUpResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: SignUpPersonalRequest): Result {
        return try {
            Result.Success(authRepository.signUpPersonal(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}