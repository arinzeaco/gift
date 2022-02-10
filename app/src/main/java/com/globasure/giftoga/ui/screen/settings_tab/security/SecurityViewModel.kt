package com.globasure.giftoga.ui.screen.settings_tab.security

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.ResetPasswordUseCase
import com.globasure.giftoga.domain.usecase.ResetTokenUseCase
import com.globasure.giftoga.network.request.ResetPasswordRequest
import com.globasure.giftoga.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SecurityViewModel @Inject constructor(
    private val requestTokenUseCase: ResetTokenUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
) : BaseViewModel<SecurityFragView>() {

    fun requestToken(email: String) {
        handleTokenResult(ResetTokenUseCase.Result.Loading)
        viewModelScope.launch {
            handleTokenResult(requestTokenUseCase.execute(email))
        }
    }

    private fun handleTokenResult(result: ResetTokenUseCase.Result) {
        when (result) {
            is ResetTokenUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is ResetTokenUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.requestTokenSuccess()
            }
            is ResetTokenUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    fun updatePassword(request: ResetPasswordRequest) {
        handlePasswordResult(ResetPasswordUseCase.Result.Loading)
        viewModelScope.launch {
            handlePasswordResult(resetPasswordUseCase.execute(request))
        }
    }

    private fun handlePasswordResult(result: ResetPasswordUseCase.Result) {
        when (result) {
            is ResetPasswordUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is ResetPasswordUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.updatePasswordSuccess()
            }
            is ResetPasswordUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }
}