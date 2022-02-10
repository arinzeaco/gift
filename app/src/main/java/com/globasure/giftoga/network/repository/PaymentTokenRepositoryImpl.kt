package com.globasure.giftoga.network.repository

import com.globasure.giftoga.domain.repository.PaymentTokenRepository
import com.globasure.giftoga.network.remote.ApiHelper
import com.globasure.giftoga.network.request.CardOtpRequest
import com.globasure.giftoga.network.request.CardPinRequest
import com.globasure.giftoga.network.request.ChargeCardRequest
import com.globasure.giftoga.network.request.ChargeFeesRequest
import com.globasure.giftoga.network.request.ChargeGiftCardRequest
import com.globasure.giftoga.network.request.ChargeWalletRequest
import com.globasure.giftoga.network.request.GetLinkForBulkRequest
import com.globasure.giftoga.network.request.GetLinkForPaymentRequest
import com.globasure.giftoga.network.request.GetLinkForSalesRequest
import com.globasure.giftoga.network.response.CardOtpResponse
import com.globasure.giftoga.network.response.CardPinResponse
import com.globasure.giftoga.network.response.ChargeBankTransferResponse
import com.globasure.giftoga.network.response.ChargeCardResponse
import com.globasure.giftoga.network.response.ChargeFeesResponse
import com.globasure.giftoga.network.response.ChargePayPalResponse
import com.globasure.giftoga.network.response.ChargeWalletResponse
import com.globasure.giftoga.network.response.PayLinkResponse

class PaymentTokenRepositoryImpl(
    private val apiHelper: ApiHelper
) : PaymentTokenRepository {

    override suspend fun getPayLinkForSales(request: GetLinkForSalesRequest): PayLinkResponse {
        return apiHelper.getPayLinkForSales(request)
    }

    override suspend fun getPayLinkForBulk(request: GetLinkForBulkRequest): PayLinkResponse {
        return apiHelper.getPayLinkForBulk(request)
    }

    override suspend fun getLinkForPayment(request: GetLinkForPaymentRequest): PayLinkResponse {
        return apiHelper.getLinkForPayment(request)
    }

    override suspend fun chargeWallet(request: ChargeWalletRequest): ChargeWalletResponse {
        return apiHelper.chargeWallet(request)
    }

    override suspend fun chargeGiftCard(request: ChargeGiftCardRequest): ChargeWalletResponse {
        return apiHelper.chargeGiftCard(request)
    }

    override suspend fun chargeCard(request: ChargeCardRequest): ChargeCardResponse {
        return apiHelper.chargeCard(request)
    }

    override suspend fun chargeFees(request: ChargeFeesRequest): ChargeFeesResponse {
        return apiHelper.chargeFees(request)
    }

    override suspend fun cardPin(request: CardPinRequest): CardPinResponse {
        return apiHelper.cardPin(request)
    }

    override suspend fun cardOtp(request: CardOtpRequest): CardOtpResponse {
        return apiHelper.cardOtp(request)
    }

    override suspend fun chargeBankTransfer(pay_link: String): ChargeBankTransferResponse {
        return apiHelper.chargeBankTransfer(pay_link)
    }

    override suspend fun confirmBankTransfer(reference: String): ChargeBankTransferResponse {
        return apiHelper.confirmBankTransfer(reference)
    }

    override suspend fun chargePayPal(pay_link: String): ChargePayPalResponse {
        return apiHelper.chargePayPal(pay_link)
    }

    override suspend fun confirmPayPal(reference: String, order_id: String): ChargePayPalResponse {
        return apiHelper.confirmPayPal(reference, order_id)
    }
}