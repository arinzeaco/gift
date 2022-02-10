package com.globasure.giftoga.domain.repository

import com.globasure.giftoga.network.response.AddCardResponse
import com.globasure.giftoga.network.response.ListCardResponse
import com.globasure.giftoga.network.response.RemoveCardResponse

interface WalletRepository {

    suspend fun addBankCard(payment_token: String): AddCardResponse

    suspend fun getListBankCards(order_by: String, page: Int, per_page: Int): ListCardResponse

    suspend fun deleteBankCard(card_token: String): RemoveCardResponse
}