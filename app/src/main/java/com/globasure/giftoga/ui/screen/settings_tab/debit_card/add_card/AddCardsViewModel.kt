package com.globasure.giftoga.ui.screen.settings_tab.debit_card.add_card

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.AddCardUseCase
import com.globasure.giftoga.domain.usecase.AddChargeCardUseCase
import com.globasure.giftoga.network.request.ChargeCardRequest
import com.globasure.giftoga.network.request.GetLinkForPaymentRequest
import com.globasure.giftoga.ui.base.BaseViewModel
import com.globasure.giftoga.utils.extension.runIfNotNull
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class AddCardsViewModel @Inject constructor(
    private val addCardUseCase: AddCardUseCase,
    private val addChargeCardUseCase: AddChargeCardUseCase
) : BaseViewModel<AddCardsView>() {

    fun startAddCard(request: GetLinkForPaymentRequest, chargeCardRequest: ChargeCardRequest) {
        viewModelScope.launch {
            handleChargeCardResult(addChargeCardUseCase.execute(request, chargeCardRequest))
        }
    }

    private fun handleChargeCardResult(result: AddChargeCardUseCase.Result) {
        when (result) {
            is AddChargeCardUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is AddChargeCardUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                if (result.response.message.contains("card_pin")) {
                    getView()?.moveToCardPin(result.response.data.reference)
                } else {
                    result.response.data.paymentToken.runIfNotNull {
                        addCard(it)
                    }
                }
            }
            is AddChargeCardUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun addCard(paymentToken: String) {
        viewModelScope.launch {
            handleCardResult(addCardUseCase.execute(paymentToken))
        }
    }

    private fun handleCardResult(result: AddCardUseCase.Result) {
        when (result) {
            is AddCardUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is AddCardUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.addCardSuccess()
            }
            is AddCardUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }
}