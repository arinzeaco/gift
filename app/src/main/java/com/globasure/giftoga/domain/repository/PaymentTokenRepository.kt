package com.globasure.giftoga.domain.repository

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

interface PaymentTokenRepository {
    suspend fun getPayLinkForSales(request: GetLinkForSalesRequest): PayLinkResponse

    suspend fun getPayLinkForBulk(request: GetLinkForBulkRequest): PayLinkResponse

//    suspend fun getPayLinkForCard(request: GetLinkForCardRequest): PayLinkResponse

    suspend fun getLinkForPayment(request: GetLinkForPaymentRequest): PayLinkResponse

    suspend fun chargeWallet(request: ChargeWalletRequest): ChargeWalletResponse

    suspend fun chargeGiftCard(request: ChargeGiftCardRequest): ChargeWalletResponse

    suspend fun chargeCard(request: ChargeCardRequest): ChargeCardResponse

    suspend fun chargeFees(request: ChargeFeesRequest): ChargeFeesResponse

    suspend fun cardPin(request: CardPinRequest): CardPinResponse

    suspend fun cardOtp(request: CardOtpRequest): CardOtpResponse

    suspend fun chargeBankTransfer(pay_link: String): ChargeBankTransferResponse

    suspend fun confirmBankTransfer(reference: String): ChargeBankTransferResponse

    suspend fun chargePayPal(pay_link: String): ChargePayPalResponse

    suspend fun confirmPayPal(reference: String, order_id: String): ChargePayPalResponse
}