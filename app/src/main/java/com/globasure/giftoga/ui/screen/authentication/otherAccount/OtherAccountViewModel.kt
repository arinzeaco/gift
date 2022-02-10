package com.globasure.giftoga.ui.screen.authentication.otherAccount

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.GetCountryListUseCase
import com.globasure.giftoga.domain.usecase.GetStateByCountryUseCase
import com.globasure.giftoga.domain.usecase.SignUpAgentUseCase
import com.globasure.giftoga.domain.usecase.SignUpBusinessUseCase
import com.globasure.giftoga.domain.usecase.SignUpMerchantUseCase
import com.globasure.giftoga.network.request.SignUpOtherRequest
import com.globasure.giftoga.ui.base.BaseViewModel
import com.globasure.giftoga.utils.HawkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class OtherAccountViewModel @Inject constructor(
    private val signUpBusinessUseCase: SignUpBusinessUseCase,
    private val signUpAgentUseCase: SignUpAgentUseCase,
    private val signUpMerchantUseCase: SignUpMerchantUseCase,
    private val getCountryListUseCase: GetCountryListUseCase,
    private val getStateByCountryUseCase: GetStateByCountryUseCase,
    private val hawkHelper: HawkHelper
) : BaseViewModel<OtherAccountView>() {

    fun doSignUpBusiness(signUpOtherRequest: SignUpOtherRequest) {
        viewModelScope.launch {
            handleSignUpBusinessResult(signUpBusinessUseCase.execute(signUpOtherRequest))
        }
    }

    private fun handleSignUpBusinessResult(result: SignUpBusinessUseCase.Result) {
        when (result) {
            is SignUpBusinessUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is SignUpBusinessUseCase.Result.Success -> {
                hawkHelper.setToken(result.response.data.token)
                getView()?.showProgressDialog(false)
                getView()?.signUpSuccess()
            }
            is SignUpBusinessUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    fun doSignUpAgent(signUpOtherRequest: SignUpOtherRequest) {
        viewModelScope.launch {
            handleSignUpAgentResult(signUpAgentUseCase.execute(signUpOtherRequest))
        }
    }

    private fun handleSignUpAgentResult(result: SignUpAgentUseCase.Result) {
        when (result) {
            is SignUpAgentUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is SignUpAgentUseCase.Result.Success -> {
                hawkHelper.setToken(result.response.data.token)
                getView()?.showProgressDialog(false)
                getView()?.signUpSuccess()
            }
            is SignUpAgentUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    fun doSignUpSignUpMerchant(signUpOtherRequest: SignUpOtherRequest) {
        viewModelScope.launch {
            handleSignUpMerchantResult(signUpMerchantUseCase.execute(signUpOtherRequest))
        }
    }

    private fun handleSignUpMerchantResult(result: SignUpMerchantUseCase.Result) {
        when (result) {
            is SignUpMerchantUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is SignUpMerchantUseCase.Result.Success -> {
                hawkHelper.setToken(result.response.data.token)
                getView()?.showProgressDialog(false)
                getView()?.signUpSuccess()
            }
            is SignUpMerchantUseCase.Result.Failure -> {
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