package com.globasure.giftoga.network.repository

import com.globasure.giftoga.domain.repository.TransferRepository
import com.globasure.giftoga.network.remote.ApiHelper
import com.globasure.giftoga.network.request.TransferCommissionRequest
import com.globasure.giftoga.network.response.BankListResponse
import com.globasure.giftoga.network.response.TransferCommissionResponse
import com.globasure.giftoga.network.response.ValidateBankAccountResponse

class TransferRepositoryImpl(
    private val apiHelper: ApiHelper
) : TransferRepository {
    override suspend fun transferCommission(request: TransferCommissionRequest): TransferCommissionResponse {
        return apiHelper.transferCommission(request)
    }

    override suspend fun transferRedeem(request: TransferCommissionRequest): TransferCommissionResponse {
        return apiHelper.transferRedeem(request)
    }

    override suspend fun getBanks(): BankListResponse {
        return apiHelper.getBanks()
    }

    override suspend fun validateBankAccount(
        paymentMethod: String,
        bankCode: String,
        accountId: String
    ): ValidateBankAccountResponse {
        return apiHelper.validateBankAccount(paymentMethod, bankCode, accountId)
    }
}