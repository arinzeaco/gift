package com.globasure.giftoga.domain.repository

import com.globasure.giftoga.network.request.BuyGiftcardRequest
import com.globasure.giftoga.network.request.MerchantsRequest
import com.globasure.giftoga.network.request.MyGiftCardRequest
import com.globasure.giftoga.network.request.SendGiftCardRequest
import com.globasure.giftoga.network.response.*

interface GiftCardRepository {
    suspend fun getAllMerchants(request: MerchantsRequest): MerchantsResponse

    suspend fun getMerchantDetail(merchant_id: String): MerchantDetailResponse

    suspend fun getMerchantByCategoryId(category_id: String): MerchantsResponse

    suspend fun getAllCategories(): CategoriesResponse

    suspend fun getCategoryDetail(category_id: String): CategoryResponse

    suspend fun getGiftCardDetail(giftcard: String): GiftcardDetailResponse

    suspend fun buyGiftCard(request: BuyGiftcardRequest): BuyGiftCardResponse

    suspend fun redeemGiftCard(giftcard: String, amount: String): RedeemGiftCardResponse

    suspend fun getMyGiftCard(request: MyGiftCardRequest): MyGiftCardResponse

    suspend fun sendMyGiftCard(request: SendGiftCardRequest): SendGiftCardResponse
}