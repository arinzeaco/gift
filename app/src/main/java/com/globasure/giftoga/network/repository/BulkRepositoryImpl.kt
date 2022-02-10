package com.globasure.giftoga.network.repository

import com.globasure.giftoga.domain.repository.BulkRepository
import com.globasure.giftoga.network.remote.ApiHelper
import com.globasure.giftoga.network.response.PurchaseBulkResponse
import com.globasure.giftoga.network.response.ValidateBulkFileResponse
import okhttp3.MultipartBody

class BulkRepositoryImpl(
    private val apiHelper: ApiHelper
) : BulkRepository {
    override suspend fun validateBulkFile(
        merchant: String,
        excelFile: MultipartBody.Part?
    ): ValidateBulkFileResponse {
        return apiHelper.validateBulkFile(merchant, excelFile)
    }

    override suspend fun purchasesBulk(payment_token: String): PurchaseBulkResponse {
        return apiHelper.purchasesBulk(payment_token)
    }
}