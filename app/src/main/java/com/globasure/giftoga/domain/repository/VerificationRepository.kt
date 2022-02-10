package com.globasure.giftoga.domain.repository

import com.globasure.giftoga.network.response.VerificationResponse
import okhttp3.MultipartBody

interface VerificationRepository {

    suspend fun verifyBusiness(
        document_file: MultipartBody.Part?,
        document_type: String,
        bvn: String,
        rc_number: String
    ): VerificationResponse

    suspend fun verifyCustomer(
        document_file: MultipartBody.Part?,
        document_type: String,
        bvn: String
    ): VerificationResponse

    suspend fun getVerificationStatus(): VerificationResponse
}