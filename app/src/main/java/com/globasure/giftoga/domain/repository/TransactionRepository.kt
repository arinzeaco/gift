package com.globasure.giftoga.domain.repository

import com.globasure.giftoga.network.request.TransactionRequest
import com.globasure.giftoga.network.response.DepositResponse
import com.globasure.giftoga.network.response.ExportTransactionResponse
import com.globasure.giftoga.network.response.TransactionResponse

interface TransactionRepository {
    suspend fun deposit(payment_token: String): DepositResponse

    suspend fun getAllTransaction(request: TransactionRequest): TransactionResponse

    suspend fun getTransactionDetail(reference: String): DepositResponse

    suspend fun exportTransaction(request: TransactionRequest): ExportTransactionResponse
}