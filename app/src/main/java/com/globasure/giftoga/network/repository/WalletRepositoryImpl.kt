package com.globasure.giftoga.network.repository

import com.globasure.giftoga.domain.repository.WalletRepository
import com.globasure.giftoga.network.remote.ApiHelper
import com.globasure.giftoga.network.response.AddCardResponse
import com.globasure.giftoga.network.response.ListCardResponse
import com.globasure.giftoga.network.response.RemoveCardResponse

class WalletRepositoryImpl(
    private val apiHelper: ApiHelper
) : WalletRepository {

    override suspend fun addBankCard(payment_token: String): AddCardResponse {
        return apiHelper.addBankCard(payment_token)
    }

    override suspend fun getListBankCards(order_by: String, page: Int, per_page: Int): ListCardResponse {
        return apiHelper.getListBankCards(order_by, page, per_page)
    }

    override suspend fun deleteBankCard(card_token: String): RemoveCardResponse {
        return apiHelper.deleteBankCard(card_token)
    }
}