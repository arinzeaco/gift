package com.globasure.giftoga.ui.screen.card_otp

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.constant.BuySendType
import com.globasure.giftoga.constant.PaymentType
import com.globasure.giftoga.domain.usecase.AddCardUseCase
import com.globasure.giftoga.domain.usecase.BuyGiftCardUseCase
import com.globasure.giftoga.domain.usecase.CardOtpUseCase
import com.globasure.giftoga.domain.usecase.DepositUseCase
import com.globasure.giftoga.network.request.BuyGiftcardRequest
import com.globasure.giftoga.network.request.CardOtpRequest
import com.globasure.giftoga.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class CardOtpViewModel @Inject constructor(
    private val depositUseCase: DepositUseCase,
    private val cardOptUseCase: CardOtpUseCase,
    private val buyGiftCardUseCase: BuyGiftCardUseCase,
    private val addCardUseCase: AddCardUseCase
) : BaseViewModel<CardOtpView>() {

    fun cardOtp(cardOtpRequest: CardOtpRequest, paymentType: String?) {
        handleCardOtpResult(CardOtpUseCase.Result.Loading, paymentType)

        viewModelScope.launch {
            handleCardOtpResult(cardOptUseCase.execute(cardOtpRequest), paymentType)
        }
    }

    private fun depositUse(payment_token: String) {
        handleDepositResult(DepositUseCase.Result.Loading)

        viewModelScope.launch {
            handleDepositResult(depositUseCase.execute(payment_token))
        }
    }

    private fun addCard(paymentToken: String) {
        handleCardCardResult(AddCardUseCase.Result.Loading)

        viewModelScope.launch {
            handleCardCardResult(addCardUseCase.execute(paymentToken))
        }
    }

    private fun handleCardOtpResult(
        result: CardOtpUseCase.Result,
        paymentType: String?
    ) {
        when (result) {
            is CardOtpUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is CardOtpUseCase.Result.Success -> {
                try {
                    if (result.response.message.contentEquals(GO_CARD_PIN)) {
                        getView()?.showProgressDialog(false)
                        getView()?.moveToPin(result.response.data.reference)
                    } else {
                        when {
                            paymentType!!.contentEquals(PaymentType.FUND_WALLET.type) -> {
                                depositUse(result.response.data.paymentToken)
                            }
                            paymentType.contentEquals(PaymentType.ADD_CARD.type) -> {
                                addCard(result.response.data.paymentToken)
                            }
                            else -> {
                                val buyGiftcardRequest = BuyGiftcardRequest(
                                    payment_token = result.response.data.paymentToken,
                                    buyType = BuySendType.SELF.type,
                                    friendName = "",
                                    friendEmail = "",
                                    friendPhone = "",
                                    message = ""
                                )
                                buyGiftCard(buyGiftcardRequest)
                            }
                        }
                    }
                } catch (e: Exception) {
                }
            }
            is CardOtpUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun handleDepositResult(result: DepositUseCase.Result) {
        when (result) {
            is DepositUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is DepositUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.fundSuccess(result.response.message)
            }
            is DepositUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun handleCardCardResult(result: AddCardUseCase.Result) {
        when (result) {
            is AddCardUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is AddCardUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.addCardSuccess(result.response.data.message)
            }
            is AddCardUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun buyGiftCard(buyGiftcardRequest: BuyGiftcardRequest) {
        handleBuyGiftCardResult(BuyGiftCardUseCase.Result.Loading)

        viewModelScope.launch {
            handleBuyGiftCardResult(buyGiftCardUseCase.execute(buyGiftcardRequest))
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

    companion object {
        private const val GO_CARD_PIN = "Card Charge not Complete. Please go to next step card_pin"
    }
}