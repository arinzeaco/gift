package com.globasure.giftoga.ui.screen.settings_tab.address

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.GetCountryListUseCase
import com.globasure.giftoga.domain.usecase.GetStateByCountryUseCase
import com.globasure.giftoga.domain.usecase.UpdateAddressUseCase
import com.globasure.giftoga.network.request.UpdateAddressRequest
import com.globasure.giftoga.ui.base.BaseViewModel
import com.globasure.giftoga.utils.HawkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val updateAddressUseCase: UpdateAddressUseCase,
    private val getCountryListUseCase: GetCountryListUseCase,
    private val getStateByCountryUseCase: GetStateByCountryUseCase,
    private val hawkHelper: HawkHelper
) : BaseViewModel<AddressFragView>() {

    fun updateAddress(request: UpdateAddressRequest) {
        handleAddressResult(UpdateAddressUseCase.Result.Loading)
        viewModelScope.launch {
            handleAddressResult(updateAddressUseCase.execute(request))
        }
    }

    private fun handleAddressResult(result: UpdateAddressUseCase.Result) {
        when (result) {
            is UpdateAddressUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is UpdateAddressUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                result.response.data.user?.let {
                    hawkHelper.setUserDetail(it)
                    getView()?.updateAddressSuccess()
                }
            }
            is UpdateAddressUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    fun getCountryList() {
        viewModelScope.launch {
            handleCountryResult(getCountryListUseCase.execute())
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

    fun getStateByCountry(country: String) {
        viewModelScope.launch {
            handleStateResult(getStateByCountryUseCase.execute(country))
        }
    }

    private fun handleStateResult(result: GetStateByCountryUseCase.Result) {
        when (result) {
            is GetStateByCountryUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is GetStateByCountryUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.getStateList(result.response.data)
            }
            is GetStateByCountryUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }
}