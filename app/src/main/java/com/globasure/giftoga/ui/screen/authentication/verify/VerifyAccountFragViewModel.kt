package com.globasure.giftoga.ui.screen.authentication.verify

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.AccountConfirmUseCase
import com.globasure.giftoga.ui.base.BaseViewModel
import com.globasure.giftoga.utils.HawkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class VerifyAccountFragViewModel @Inject constructor(
    private val accountConfirmUseCase: AccountConfirmUseCase,
    private val hawkHelper: HawkHelper
) : BaseViewModel<VerifyAccountFragView>() {

    fun verifyAccount(securityToken: String) {
        handleAccountConfirmResult(AccountConfirmUseCase.Result.Loading)
        viewModelScope.launch {
            handleAccountConfirmResult(accountConfirmUseCase.execute(securityToken))
        }
    }

    private fun handleAccountConfirmResult(result: AccountConfirmUseCase.Result) {
        when (result) {
            is AccountConfirmUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is AccountConfirmUseCase.Result.Success -> {
                hawkHelper.setToken(result.response.data.token)
                getView()?.showProgressDialog(false)
                getView()?.verifyEmailDone()
            }
            is AccountConfirmUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }
}