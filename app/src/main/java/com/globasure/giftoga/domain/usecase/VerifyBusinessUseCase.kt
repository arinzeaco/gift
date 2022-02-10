package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.VerificationRepository
import com.globasure.giftoga.network.response.VerificationResponse
import okhttp3.MultipartBody
import javax.inject.Inject

class VerifyBusinessUseCase @Inject constructor(private val verificationRepository: VerificationRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: VerificationResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(documentFile: MultipartBody.Part, documentType: String, bvn: String, rcNumber: String): Result {
        return try {
            Result.Success(verificationRepository.verifyBusiness(documentFile, documentType, bvn, rcNumber))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}