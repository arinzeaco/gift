package com.globasure.giftoga.ui.screen.authentication.login

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.LoginUseCase
import com.globasure.giftoga.network.request.LoginRequest
import com.globasure.giftoga.ui.base.BaseViewModel
import com.globasure.giftoga.utils.HawkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class LoginFragViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val hawkHelper: HawkHelper
) : BaseViewModel<LoginFragView>() {

    fun doLogin(email: String, password: String) {
        handleLoginResult(LoginUseCase.Result.Loading)

        val loginRequest = LoginRequest(
            username = email,
            password = password
        )

        viewModelScope.launch {
            handleLoginResult(loginUseCase.execute(loginRequest))
        }
    }

    private fun handleLoginResult(result: LoginUseCase.Result) {
        when (result) {
            is LoginUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is LoginUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                hawkHelper.setToken(result.response.data.token)
                hawkHelper.setRefreshToken(result.response.refreshToken)
                getView()?.loginSuccess()
            }
            is LoginUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }
}