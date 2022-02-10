package com.globasure.giftoga.ui.screen.settings_tab.verification

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.GetBanksUseCase
import com.globasure.giftoga.domain.usecase.VerifyBusinessUseCase
import com.globasure.giftoga.domain.usecase.VerifyCustomerUseCase
import com.globasure.giftoga.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import timber.log.Timber

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val verifyBusinessUseCase: VerifyBusinessUseCase,
    private val verifyCustomerUseCase: VerifyCustomerUseCase,
    private val getBanksUseCase: GetBanksUseCase
) : BaseViewModel<VerificationFragView>() {

    fun getBankList() {
        viewModelScope.launch {
            handleBanksResult(getBanksUseCase.execute())
        }
    }

    private fun handleBanksResult(result: GetBanksUseCase.Result) {
        when (result) {
            is GetBanksUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is GetBanksUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.getBanksSuccess(result.response.data.bank)
            }
            is GetBanksUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    fun verifyDocument(
        isCustomer: Boolean,
        documentFile: MultipartBody.Part,
        documentType: String,
        bvn: String,
        rcNumber: String
    ) {
        if (isCustomer) {
            verifyCustomer(documentFile, documentType, bvn)
        } else {
            verifyBusiness(documentFile, documentType, bvn, rcNumber)
        }
    }

    private fun verifyCustomer(documentFile: MultipartBody.Part, documentType: String, bvn: String) {
        viewModelScope.launch {
            handleCustomerResult(verifyCustomerUseCase.execute(documentFile, documentType, bvn))
        }
    }

    private fun handleCustomerResult(result: VerifyCustomerUseCase.Result) {
        when (result) {
            is VerifyCustomerUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is VerifyCustomerUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.verifySuccess()
            }
            is VerifyCustomerUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun verifyBusiness(documentFile: MultipartBody.Part, documentType: String, bvn: String, rcNumber: String) {
        viewModelScope.launch {
            handleBusinessResult(verifyBusinessUseCase.execute(documentFile, documentType, bvn, rcNumber))
        }
    }

    private fun handleBusinessResult(result: VerifyBusinessUseCase.Result) {
        when (result) {
            is VerifyBusinessUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is VerifyBusinessUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.verifySuccess()
            }
            is VerifyBusinessUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }
}