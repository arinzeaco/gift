package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.SettingRepository
import com.globasure.giftoga.network.request.AllowDailyTransferRequest
import com.globasure.giftoga.network.response.ProfileResponse
import javax.inject.Inject

class AllowDailyTransferUseCase @Inject constructor(private val settingRepository: SettingRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: ProfileResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: AllowDailyTransferRequest): Result {
        return try {
            Result.Success(settingRepository.allowDailyTransfer(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}