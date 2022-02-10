package com.globasure.giftoga.ui.screen.main_fragment.buy_cards

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.GetAllMerchantsUseCase
import com.globasure.giftoga.domain.usecase.GetCountryListUseCase
import com.globasure.giftoga.network.request.MerchantsRequest
import com.globasure.giftoga.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class BuyCardsFragViewModel @Inject constructor(
    private val getCountryListUseCase: GetCountryListUseCase,
    private val getAllMerchantsUseCase: GetAllMerchantsUseCase,
) : BaseViewModel<BuyCardsFragView>() {
    fun getCountryList() {
        viewModelScope.launch {
            handleCountryResult(getCountryListUseCase.execute())
        }
    }

    fun getGetAllMerchant(request: MerchantsRequest) {
        viewModelScope.launch {
            handleGetAllMerchant(getAllMerchantsUseCase.execute(request))
        }
    }

    private fun handleCountryResult(result: GetCountryListUseCase.Result) {
        when (result) {
            is GetCountryListUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is GetCountryListUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.getCountryList(result.response.data)
            }
            is GetCountryListUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun handleGetAllMerchant(result: GetAllMerchantsUseCase.Result) {
        when (result) {
            is GetAllMerchantsUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is GetAllMerchantsUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.getMerchantsGiftCard(result.response.data.merchant, result.response.data.meta)
            }
            is GetAllMerchantsUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.getMerchantsGiftCard(emptyList(), null)
            }
        }
    }
}