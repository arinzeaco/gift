package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.SettingRepository
import com.globasure.giftoga.network.response.BankListResponse
import javax.inject.Inject

class GetBankListUseCase @Inject constructor(private val settingRepository: SettingRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: BankListResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(): Result {
        return try {
            Result.Success(settingRepository.getBankList())
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}