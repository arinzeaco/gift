package com.globasure.giftoga.domain.repository

import com.globasure.giftoga.network.response.PurchaseBulkResponse
import com.globasure.giftoga.network.response.ValidateBulkFileResponse
import okhttp3.MultipartBody

interface BulkRepository {
    suspend fun validateBulkFile(
        merchant: String,
        excelFile: MultipartBody.Part?
    ): ValidateBulkFileResponse

    suspend fun purchasesBulk(payment_token: String): PurchaseBulkResponse
}