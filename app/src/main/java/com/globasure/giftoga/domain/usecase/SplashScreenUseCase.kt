package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.AuthRepository
import com.globasure.giftoga.network.response.ProfileResponse
import com.globasure.giftoga.utils.HawkHelper
import javax.inject.Inject

class SplashScreenUseCase @Inject constructor(
    private val hawkHelper: HawkHelper,
    private val authRepository: AuthRepository
) {
    sealed class Result {
        object Loading : Result()
        data class Success(val response: ProfileResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(): Result {
        return try {
            if (hawkHelper.getRefreshToken().isNotEmpty()) {
                //refresh token
                val loginResponse = authRepository.refreshToken(hawkHelper.getRefreshToken())
                hawkHelper.setToken(loginResponse.data.token)
                hawkHelper.setRefreshToken(loginResponse.refreshToken)

                //get user detail
                val accountResponse = authRepository.getAccountDetails()
                accountResponse.data.user?.let {
                    hawkHelper.setUserDetail(it)
                }

                Result.Success(accountResponse)
            } else {
                Result.Failure(Exception("Refresh token not found!"))
            }
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}