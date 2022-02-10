package com.globasure.giftoga.ui.screen.authentication.personalAccount

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.GetCountryListUseCase
import com.globasure.giftoga.domain.usecase.SignUpUseCase
import com.globasure.giftoga.network.request.SignUpPersonalRequest
import com.globasure.giftoga.ui.base.BaseViewModel
import com.globasure.giftoga.utils.HawkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class PersonalAccountViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val getCountryListUseCase: GetCountryListUseCase,
    private val hawkHelper: HawkHelper
) : BaseViewModel<PersonalAccountView>() {

    fun doSignUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        mobilePhone: String,
        country: String
    ) {
        handleSignUpResult(SignUpUseCase.Result.Loading)

        val signUpPersonalRequest = SignUpPersonalRequest(
            first_name = firstName,
            last_name = lastName,
            email = email,
            country = country,
            mobile = mobilePhone,
            password = password
        )
        viewModelScope.launch {
            handleSignUpResult(signUpUseCase.execute(signUpPersonalRequest))
        }
    }

    private fun handleSignUpResult(result: SignUpUseCase.Result) {
        when (result) {
            is SignUpUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is SignUpUseCase.Result.Success -> {
                hawkHelper.setToken(result.response.data.token)
                getView()?.showProgressDialog(false)
                getView()?.signUpSuccess()
            }
            is SignUpUseCase.Result.Failure -> {
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
}