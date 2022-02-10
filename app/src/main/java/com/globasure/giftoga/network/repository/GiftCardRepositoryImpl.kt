package com.globasure.giftoga.network.repository

import com.globasure.giftoga.domain.repository.GiftCardRepository
import com.globasure.giftoga.network.remote.ApiHelper
import com.globasure.giftoga.network.request.BuyGiftcardRequest
import com.globasure.giftoga.network.request.MerchantsRequest
import com.globasure.giftoga.network.request.MyGiftCardRequest
import com.globasure.giftoga.network.request.SendGiftCardRequest
import com.globasure.giftoga.network.response.*

class GiftCardRepositoryImpl(
    private val apiHelper: ApiHelper
) : GiftCardRepository {

    override suspend fun getAllMerchants(request: MerchantsRequest): MerchantsResponse {
        return apiHelper.getAllMerchants(request)
    }

    override suspend fun getMerchantDetail(merchant_id: String): MerchantDetailResponse {
        return apiHelper.getMerchantDetail(merchant_id)
    }

    override suspend fun getMerchantByCategoryId(category_id: String): MerchantsResponse {
        return apiHelper.getMerchantByCategoryId(category_id)
    }

    override suspend fun getAllCategories(): CategoriesResponse {
        return apiHelper.getAllCategories()
    }

    override suspend fun getCategoryDetail(category_id: String): CategoryResponse {
        return apiHelper.getCategoryDetail(category_id)
    }

    override suspend fun getGiftCardDetail(giftcard: String): GiftcardDetailResponse {
        return apiHelper.getGiftCardDetail(giftcard)
    }

    override suspend fun buyGiftCard(request: BuyGiftcardRequest): BuyGiftCardResponse {
        return apiHelper.buyGiftCard(request)
    }

    override suspend fun redeemGiftCard(giftcard: String, amount: String): RedeemGiftCardResponse {
        return apiHelper.redeemGiftCard(giftcard, amount)
    }

    override suspend fun getMyGiftCard(request: MyGiftCardRequest): MyGiftCardResponse {
        return apiHelper.getMyGiftCard(request)
    }

    override suspend fun sendMyGiftCard(request: SendGiftCardRequest): SendGiftCardResponse {
        return apiHelper.sendMyGiftCard(request)
    }

}
