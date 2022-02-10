package com.globasure.giftoga.ui.screen.complete_order

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.BuyGiftCardUseCase
import com.globasure.giftoga.domain.usecase.ChargeBankTransferUseCase
import com.globasure.giftoga.domain.usecase.ChargeCardUseCase
import com.globasure.giftoga.domain.usecase.ChargeFeesUseCase
import com.globasure.giftoga.domain.usecase.ChargeGiftCardUseCase
import com.globasure.giftoga.domain.usecase.ChargePayPalUseCase
import com.globasure.giftoga.domain.usecase.ChargeWalletUseCase
import com.globasure.giftoga.domain.usecase.ConfirmBankTransferUseCase
import com.globasure.giftoga.domain.usecase.ConfirmPayPalUseCase
import com.globasure.giftoga.domain.usecase.GetPayLinkForSalesUseCase
import com.globasure.giftoga.network.request.BuyGiftcardRequest
import com.globasure.giftoga.network.request.ChargeCardRequest
import com.globasure.giftoga.network.request.ChargeFeesRequest
import com.globasure.giftoga.network.request.ChargeGiftCardRequest
import com.globasure.giftoga.network.request.ChargeWalletRequest
import com.globasure.giftoga.network.request.GetLinkForSalesRequest
import com.globasure.giftoga.network.request.SendToFriendRequest
import com.globasure.giftoga.ui.base.BaseViewModel
import com.globasure.giftoga.utils.extension.runIfNotNull
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class CompleteOrderViewModel @Inject constructor(
    private val chargeFeesUseCase: ChargeFeesUseCase,
    private val chargeCardUseCase: ChargeCardUseCase,
    private val buyGiftCardUseCase: BuyGiftCardUseCase,
    private val chargeWalletUseCase: ChargeWalletUseCase,
    private val chargeGiftCardUseCase: ChargeGiftCardUseCase,
    private val getPayLinkForSalesUseCase: GetPayLinkForSalesUseCase,
    private val chargeBankTransferUseCase: ChargeBankTransferUseCase,
    private val confirmBankTransferUseCase: ConfirmBankTransferUseCase,
    private val chargePayPalUseCase: ChargePayPalUseCase,
    private val confirmPayPalUseCase: ConfirmPayPalUseCase
) : BaseViewModel<CompleteOrderFragView>() {

    private var bankTransferReference: String? = null
    var paypalReference: String? = null
    var paypalOrderId: String? = null
    private var clientId: String? = null
    private var returnURL: String? = null
    private var paypalAmount: String? = null

    fun getFees(
        currency_name: String,
        charge_type: String,
        method: String,
        amount: String,
        merchantId: String?,
        delivery_to_sms: String?,
        delivery_country: String?
    ) {
        val chargeFeesRequest = ChargeFeesRequest(
            deliveryToSms = delivery_to_sms,
            deliveryCountry = delivery_country,
            currencyName = currency_name,
            chargeType = charge_type,
            method = method,
            amount = amount,
            merchantId = merchantId
        )

        viewModelScope.launch {
            chargeFeesUseCaseResult(chargeFeesUseCase.execute(chargeFeesRequest))
        }
    }

    private fun chargeCard(
        chargeCardRequest: ChargeCardRequest,
        sendToFriendRequest: SendToFriendRequest
    ) {
        handleChargeCardResult(ChargeCardUseCase.Result.Loading, sendToFriendRequest)

        viewModelScope.launch {
            handleChargeCardResult(chargeCardUseCase.execute(chargeCardRequest), sendToFriendRequest)
        }
    }

    private fun chargeBankTransfer(paylink: String) {
        handleChargeBankTransferResult(ChargeBankTransferUseCase.Result.Loading)

        viewModelScope.launch {
            handleChargeBankTransferResult(chargeBankTransferUseCase.execute(paylink))
        }
    }

    fun confirmBankTransfer(sendToFriendRequest: SendToFriendRequest) {
        handleConfirmBankTransferResult(ConfirmBankTransferUseCase.Result.Loading, sendToFriendRequest)

        viewModelScope.launch {
            if (bankTransferReference != null) {
                handleConfirmBankTransferResult(
                    confirmBankTransferUseCase.execute(bankTransferReference!!),
                    sendToFriendRequest
                )
            }
        }
    }

    private fun chargePayPal(paylink: String) {
        viewModelScope.launch {
            handleChargePayPalResult(chargePayPalUseCase.execute(paylink))
        }
    }

    fun confirmPayStack(sendToFriendRequest: SendToFriendRequest) {
        handleConfirmPayPalResult(ConfirmPayPalUseCase.Result.Loading, sendToFriendRequest)

        viewModelScope.launch {
            if (paypalReference != null) {
                handleConfirmPayPalResult(
                    confirmPayPalUseCase.execute(paypalReference!!, paypalOrderId!!),
                    sendToFriendRequest
                )
            }
        }
    }

    fun chargeGiftCard(
        chargeGiftCardRequest: ChargeGiftCardRequest,
        sendToFriendRequest: SendToFriendRequest
    ) {
        handleChargeGiftCardWalletResult(ChargeGiftCardUseCase.Result.Loading, sendToFriendRequest)

        viewModelScope.launch {
            handleChargeGiftCardWalletResult(chargeGiftCardUseCase.execute(chargeGiftCardRequest), sendToFriendRequest)
        }
    }

    private fun buyGiftCard(buyGiftcardRequest: BuyGiftcardRequest) {
        handleBuyGiftCardResult(BuyGiftCardUseCase.Result.Loading)

        viewModelScope.launch {
            handleBuyGiftCardResult(buyGiftCardUseCase.execute(buyGiftcardRequest))
        }
    }

    fun getSalesPayLink(
        getLinkForSalesRequest: GetLinkForSalesRequest,
        chargeCardRequest: ChargeCardRequest,
        sendToFriendRequest: SendToFriendRequest
    ) {
        handleGetPayLinkForSales(GetPayLinkForSalesUseCase.Result.Loading, chargeCardRequest, sendToFriendRequest)

        viewModelScope.launch {
            handleGetPayLinkForSales(
                getPayLinkForSalesUseCase.execute(getLinkForSalesRequest),
                chargeCardRequest,
                sendToFriendRequest
            )
        }
    }

    fun getLinkForBankTransfer(getLinkForSalesRequest: GetLinkForSalesRequest) {
        handleGetPayLinkForBankTransfer(GetPayLinkForSalesUseCase.Result.Loading)
        viewModelScope.launch {
            handleGetPayLinkForBankTransfer(getPayLinkForSalesUseCase.execute(getLinkForSalesRequest))
        }
    }

    fun getLinkForPayStack(getLinkForSalesRequest: GetLinkForSalesRequest) {
        handleGetPayLinkForPayStack(GetPayLinkForSalesUseCase.Result.Loading)
        viewModelScope.launch {
            handleGetPayLinkForPayStack(getPayLinkForSalesUseCase.execute(getLinkForSalesRequest))
        }
    }

    fun getLinkForPaypal(getLinkForSalesRequest: GetLinkForSalesRequest) {
        handleGetPayLinkForPayStack(GetPayLinkForSalesUseCase.Result.Loading)
        viewModelScope.launch {
            handleGetPayLinkForPayStack(getPayLinkForSalesUseCase.execute(getLinkForSalesRequest))
        }
    }

    fun chargeWallet(
        getLinkForSalesRequest: GetLinkForSalesRequest,
        chargeWalletRequest: ChargeWalletRequest,
        sendToFriendRequest: SendToFriendRequest
    ) {
        handleChargeWalletResult(ChargeWalletUseCase.Result.Loading, sendToFriendRequest)
        viewModelScope.launch {
            handleChargeWalletResult(
                chargeWalletUseCase.execute(getLinkForSalesRequest, chargeWalletRequest), sendToFriendRequest
            )
        }
    }

    private fun chargeFeesUseCaseResult(result: ChargeFeesUseCase.Result) {
        when (result) {
            is ChargeFeesUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is ChargeFeesUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.chargeFeesSuccess(result.response.data)
            }
            is ChargeFeesUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun handleChargeCardResult(
        result: ChargeCardUseCase.Result,
        sendToFriendRequest: SendToFriendRequest
    ) {
        when (result) {
            is ChargeCardUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is ChargeCardUseCase.Result.Success -> {
                try {
                    if (result.response.message.contentEquals(GO_CARD_PIN)) {
                        getView()?.showProgressDialog(false)
                        getView()?.moveToCardPin(result.response.data.reference)
                    } else {
                        result.response.data.paymentToken.runIfNotNull {
                            val buyGiftcardRequest = BuyGiftcardRequest(
                                payment_token = it,
                                buyType = sendToFriendRequest.buyType,
                                friendName = sendToFriendRequest.friendName,
                                friendEmail = sendToFriendRequest.friendEmail,
                                friendPhone = sendToFriendRequest.friendPhone,
                                message = sendToFriendRequest.message
                            )
                            buyGiftCard(buyGiftcardRequest)
                        }
                    }
                } catch (ex: Exception) {
                    Timber.e(ex)
                }
            }
            is ChargeCardUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun handleChargeBankTransferResult(result: ChargeBankTransferUseCase.Result) {
        when (result) {
            is ChargeBankTransferUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is ChargeBankTransferUseCase.Result.Success -> {
                try {
                    getView()?.showProgressDialog(false)
                    bankTransferReference = result.response.data.reference
                    getView()?.chargeBankTransferSuccess(result.response.data)
                } catch (ex: Exception) {
                    Timber.e(ex)
                }
            }
            is ChargeBankTransferUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun handleConfirmBankTransferResult(
        result: ConfirmBankTransferUseCase.Result,
        sendToFriendRequest: SendToFriendRequest
    ) {
        when (result) {
            is ConfirmBankTransferUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is ConfirmBankTransferUseCase.Result.Success -> {
                try {
                    getView()?.showProgressDialog(false)
                    if (result.response.data.status == "paid") {
                        result.response.data.paymentToken.runIfNotNull {
                            val buyGiftcardRequest = BuyGiftcardRequest(
                                payment_token = it,
                                buyType = sendToFriendRequest.buyType,
                                friendName = sendToFriendRequest.friendName,
                                friendEmail = sendToFriendRequest.friendEmail,
                                friendPhone = sendToFriendRequest.friendPhone,
                                message = sendToFriendRequest.message
                            )
                            buyGiftCard(buyGiftcardRequest)
                        }
                    }
                } catch (ex: Exception) {
                    Timber.e(ex)
                }
            }
            is ConfirmBankTransferUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun handleChargePayPalResult(result: ChargePayPalUseCase.Result) {
        when (result) {
            is ChargePayPalUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is ChargePayPalUseCase.Result.Success -> {
                try {
                    getView()?.showProgressDialog(false)
                    paypalReference = result.response.data.reference
                    paypalOrderId = result.response.data.orderId ?: result.response.data.reference
                    clientId = result.response.data.clientId
                    paypalAmount = result.response.data.amount.toString()
                    returnURL = result.response.data.returnUrl
                    getView()?.setupPaypal(clientId.orEmpty(), returnURL.orEmpty(), paypalAmount.orEmpty())
                } catch (ex: Exception) {
                    Timber.e(ex)
                }
            }
            is ChargePayPalUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun handleConfirmPayPalResult(
        result: ConfirmPayPalUseCase.Result,
        sendToFriendRequest: SendToFriendRequest
    ) {
        when (result) {
            is ConfirmPayPalUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is ConfirmPayPalUseCase.Result.Success -> {
                try {
                    getView()?.showProgressDialog(false)

                    result.response.data.paymentToken.runIfNotNull {
                        val buyGiftcardRequest = BuyGiftcardRequest(
                            payment_token = it,
                            buyType = sendToFriendRequest.buyType,
                            friendName = sendToFriendRequest.friendName,
                            friendEmail = sendToFriendRequest.friendEmail,
                            friendPhone = sendToFriendRequest.friendPhone,
                            message = sendToFriendRequest.message
                        )
                        buyGiftCard(buyGiftcardRequest)
                    }
                } catch (ex: Exception) {
                    Timber.e(ex)
                }
            }
            is ConfirmPayPalUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun handleGetPayLinkForSales(
        result: GetPayLinkForSalesUseCase.Result,
        chargeCardRequest: ChargeCardRequest,
        sendToFriendRequest: SendToFriendRequest
    ) {
        when (result) {
            is GetPayLinkForSalesUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is GetPayLinkForSalesUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                chargeCardRequest.payLink = result.response.data.payNumber
                chargeCard(chargeCardRequest, sendToFriendRequest)

            }
            is GetPayLinkForSalesUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun handleGetPayLinkForBankTransfer(
        result: GetPayLinkForSalesUseCase.Result
    ) {
        when (result) {
            is GetPayLinkForSalesUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is GetPayLinkForSalesUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                chargeBankTransfer(result.response.data.payNumber)

            }
            is GetPayLinkForSalesUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun handleGetPayLinkForPayStack(
        result: GetPayLinkForSalesUseCase.Result
    ) {
        when (result) {
            is GetPayLinkForSalesUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is GetPayLinkForSalesUseCase.Result.Success -> {
                chargePayPal(result.response.data.payNumber)
            }
            is GetPayLinkForSalesUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun handleBuyGiftCardResult(result: BuyGiftCardUseCase.Result) {
        when (result) {
            is BuyGiftCardUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is BuyGiftCardUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.fundSuccess(result.response.message)

            }
            is BuyGiftCardUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun handleChargeWalletResult(result: ChargeWalletUseCase.Result, sendToFriendRequest: SendToFriendRequest) {
        when (result) {
            is ChargeWalletUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is ChargeWalletUseCase.Result.Success -> {
                val buyGiftcardRequest = BuyGiftcardRequest(
                    payment_token = result.response.data.paymentToken,
                    buyType = sendToFriendRequest.buyType,
                    friendName = sendToFriendRequest.friendName,
                    friendEmail = sendToFriendRequest.friendEmail,
                    friendPhone = sendToFriendRequest.friendPhone,
                    message = sendToFriendRequest.message
                )
                buyGiftCard(buyGiftcardRequest)
            }
            is ChargeWalletUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun handleChargeGiftCardWalletResult(
        result: ChargeGiftCardUseCase.Result,
        sendToFriendRequest: SendToFriendRequest
    ) {
        when (result) {
            is ChargeGiftCardUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is ChargeGiftCardUseCase.Result.Success -> {
                val buyGiftcardRequest = BuyGiftcardRequest(
                    payment_token = result.response.data.paymentToken,
                    buyType = sendToFriendRequest.buyType,
                    friendName = sendToFriendRequest.friendName,
                    friendEmail = sendToFriendRequest.friendEmail,
                    friendPhone = sendToFriendRequest.friendPhone,
                    message = sendToFriendRequest.message
                )
                buyGiftCard(buyGiftcardRequest)
            }
            is ChargeGiftCardUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    companion object {
        private const val GO_CARD_PIN = "Card Charge not Complete. Please go to next step card_pin"
    }
}