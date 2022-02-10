package com.globasure.giftoga.network.repository

import com.globasure.giftoga.domain.repository.TransactionRepository
import com.globasure.giftoga.network.remote.ApiHelper
import com.globasure.giftoga.network.request.TransactionRequest
import com.globasure.giftoga.network.response.DepositResponse
import com.globasure.giftoga.network.response.ExportTransactionResponse
import com.globasure.giftoga.network.response.TransactionResponse

class TransactionRepositoryImpl(
    private val apiHelper: ApiHelper
) : TransactionRepository {
    override suspend fun deposit(payment_token: String): DepositResponse {
        return apiHelper.deposit(payment_token)
    }

    override suspend fun getAllTransaction(request: TransactionRequest): TransactionResponse {
        return apiHelper.getAllTransaction(request)
    }

    override suspend fun getTransactionDetail(reference: String): DepositResponse {
        return apiHelper.getTransactionDetail(reference)
    }

    override suspend fun exportTransaction(request: TransactionRequest): ExportTransactionResponse {
        return apiHelper.exportTransaction(request)
    }
}