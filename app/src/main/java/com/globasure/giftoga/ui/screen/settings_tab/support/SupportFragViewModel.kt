package com.globasure.giftoga.ui.screen.settings_tab.support

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.OpenCaseUseCase
import com.globasure.giftoga.network.request.OpenCaseRequest
import com.globasure.giftoga.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SupportFragViewModel @Inject constructor(
    private val openCaseUseCase: OpenCaseUseCase
) : BaseViewModel<SupportFragView>() {

    fun submitCase(request: OpenCaseRequest) {
        handleCaseResult(OpenCaseUseCase.Result.Loading)

        viewModelScope.launch {
            handleCaseResult(openCaseUseCase.execute(request))
        }
    }

    private fun handleCaseResult(result: OpenCaseUseCase.Result) {
        when (result) {
            is OpenCaseUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is OpenCaseUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.submitCaseSuccess(result.response)
            }
            is OpenCaseUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }
}