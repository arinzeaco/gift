package com.globasure.giftoga.ui.screen.fund_wallet

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.ChargeCardUseCase
import com.globasure.giftoga.domain.usecase.DepositUseCase
import com.globasure.giftoga.domain.usecase.GetPayLinkForPaymentUseCase
import com.globasure.giftoga.network.request.ChargeCardRequest
import com.globasure.giftoga.network.request.GetLinkForPaymentRequest
import com.globasure.giftoga.ui.base.BaseViewModel
import com.globasure.giftoga.utils.extension.runIfNotNull
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class FundWalletViewModel @Inject constructor(
    private val depositUseCase: DepositUseCase,
    private val chargeCardUseCase: ChargeCardUseCase,
    private val getPayLinkForPaymentUseCase: GetPayLinkForPaymentUseCase
) : BaseViewModel<FundWalletView>() {

    fun handlePayLink(request: GetLinkForPaymentRequest, chargeCardRequest: ChargeCardRequest) {
        viewModelScope.launch {
            handlePayLinkResult(getPayLinkForPaymentUseCase.execute(request), chargeCardRequest)
        }
    }

    private fun handleChargeCard(chargeCardRequest: ChargeCardRequest) {
        handleChargeCardResult(ChargeCardUseCase.Result.Loading)

        viewModelScope.launch {
            handleChargeCardResult(chargeCardUseCase.execute(chargeCardRequest))
        }
    }

    private fun depositUseCase(payment_token: String) {
        handleDepositResult(DepositUseCase.Result.Loading)

        viewModelScope.launch {
            handleDepositResult(depositUseCase.execute(payment_token))
        }
    }

    private fun handleChargeCardResult(
        result: ChargeCardUseCase.Result
    ) {
        when (result) {
            is ChargeCardUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is ChargeCardUseCase.Result.Success -> {
                if (result.response.message.contentEquals(GO_CARD_PIN)) {
                    getView()?.showProgressDialog(false)
                    getView()?.moveToCardPin(result.response.data.reference)
                } else {
                    result.response.data.paymentToken.runIfNotNull {
                        depositUseCase(it)
                    }
                }
            }
            is ChargeCardUseCase.Result.Failure -> {
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
                getView()?.fundSuccess(result.response.message)
                getView()?.showProgressDialog(false)
            }
            is DepositUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun handlePayLinkResult(result: GetPayLinkForPaymentUseCase.Result, chargeCardRequest: ChargeCardRequest) {
        when (result) {
            is GetPayLinkForPaymentUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is GetPayLinkForPaymentUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                val payLink = result.response.data.payNumber
                chargeCardRequest.payLink = payLink
                handleChargeCard(chargeCardRequest)
            }
            is GetPayLinkForPaymentUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    companion object {
        private const val GO_CARD_PIN = "Validate Card PIN"
    }
}