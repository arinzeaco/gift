package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.BulkRepository
import com.globasure.giftoga.network.response.ValidateBulkFileResponse
import okhttp3.MultipartBody
import javax.inject.Inject

class ValidateBulkUseCase @Inject constructor(private val bulkRepository: BulkRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: ValidateBulkFileResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(merchant: String, excelFile: MultipartBody.Part): Result {
        return try {
            Result.Success(bulkRepository.validateBulkFile(merchant, excelFile))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}