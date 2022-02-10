package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.SettingRepository
import com.globasure.giftoga.network.request.ValidateBankAccountRequest
import com.globasure.giftoga.network.response.ValidateBankAccountResponse
import javax.inject.Inject

class ValidateBankAccountUseCase @Inject constructor(private val settingRepository: SettingRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: ValidateBankAccountResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(request: ValidateBankAccountRequest): Result {
        return try {
            Result.Success(settingRepository.validateBankAccount(request))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}