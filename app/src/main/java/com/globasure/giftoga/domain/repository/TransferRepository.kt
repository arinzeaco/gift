package com.globasure.giftoga.domain.repository

import com.globasure.giftoga.network.request.TransferCommissionRequest
import com.globasure.giftoga.network.response.BankListResponse
import com.globasure.giftoga.network.response.TransferCommissionResponse
import com.globasure.giftoga.network.response.ValidateBankAccountResponse

interface TransferRepository {

    suspend fun transferCommission(request: TransferCommissionRequest): TransferCommissionResponse

    suspend fun transferRedeem(request: TransferCommissionRequest): TransferCommissionResponse

    suspend fun getBanks(): BankListResponse

    suspend fun validateBankAccount(
        paymentMethod: String,
        bankCode: String,
        accountId: String
    ): ValidateBankAccountResponse
}