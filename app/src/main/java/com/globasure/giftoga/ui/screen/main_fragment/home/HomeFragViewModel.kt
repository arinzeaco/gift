package com.globasure.giftoga.ui.screen.main_fragment.home

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.GetAllTransactionUseCase
import com.globasure.giftoga.domain.usecase.GetUserDetailsUseCase
import com.globasure.giftoga.network.request.TransactionRequest
import com.globasure.giftoga.ui.base.BaseViewModel
import com.globasure.giftoga.utils.HawkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class HomeFragViewModel @Inject constructor(
    private val getAllTransactionUseCase: GetAllTransactionUseCase,
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val hawkHelper: HawkHelper
) : BaseViewModel<HomeFragView>() {

    fun getAllTransactionHistory() {
        viewModelScope.launch {
            val transactionRequest = TransactionRequest(page = 1, perPage = 20)
            handleTransactionResult(getAllTransactionUseCase.execute(transactionRequest))
        }
    }

    private fun handleTransactionResult(result: GetAllTransactionUseCase.Result) {
        when (result) {
            is GetAllTransactionUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is GetAllTransactionUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.getAllTransactionSuccess(result.response.data.transaction)
            }
            is GetAllTransactionUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                //getView()?.handleError(result.throwable)
                getView()?.getAllTransactionSuccess(emptyList())
            }
        }
    }

    fun getUserDetail() {
        viewModelScope.launch {
            handleUserDetailResult(getUserDetailsUseCase.execute())
        }
    }

    private fun handleUserDetailResult(result: GetUserDetailsUseCase.Result) {
        when (result) {
            is GetUserDetailsUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is GetUserDetailsUseCase.Result.Success -> {
                result.response.data.user?.let {
                    hawkHelper.setUserDetail(it)
                    getView()?.showProgressDialog(false)
                    getView()?.getUserDetailSuccess(result.response)
                }
            }
            is GetUserDetailsUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }
}