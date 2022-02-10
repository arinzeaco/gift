package com.globasure.giftoga.network.repository

import com.globasure.giftoga.domain.repository.VerificationRepository
import com.globasure.giftoga.network.remote.ApiHelper
import com.globasure.giftoga.network.response.VerificationResponse
import okhttp3.MultipartBody

class VerificationRepositoryImpl(
    private val apiHelper: ApiHelper
) : VerificationRepository {

    override suspend fun verifyBusiness(
        document_file: MultipartBody.Part?,
        document_type: String,
        bvn: String,
        rc_number: String
    ): VerificationResponse {
        return apiHelper.verifyBusiness(document_file, document_type, bvn, rc_number)
    }

    override suspend fun verifyCustomer(
        document_file: MultipartBody.Part?,
        document_type: String,
        bvn: String
    ): VerificationResponse {
        return apiHelper.verifyCustomer(document_file, document_type, bvn)
    }

    override suspend fun getVerificationStatus(): VerificationResponse {
        return apiHelper.getVerificationStatus()
    }

}