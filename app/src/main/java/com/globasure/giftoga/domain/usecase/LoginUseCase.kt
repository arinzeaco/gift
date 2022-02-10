package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.AuthRepository
import com.globasure.giftoga.network.request.LoginRequest
import com.globasure.giftoga.network.response.LoginResponse
import com.globasure.giftoga.utils.HawkHelper
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val hawkHelper: HawkHelper
) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: LoginResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: LoginRequest): Result {
        return try {
            //refresh token
            val loginResponse = authRepository.signIn(request)
            hawkHelper.setToken(loginResponse.data.token)
            hawkHelper.setRefreshToken(loginResponse.refreshToken)

            //get user detail
            val accountResponse = authRepository.getAccountDetails()
            accountResponse.data.user?.let {
                hawkHelper.setUserDetail(it)
            }

            Result.Success(loginResponse)
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}