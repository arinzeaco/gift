package com.globasure.giftoga.ui.screen.transactions

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.GetAllTransactionUseCase
import com.globasure.giftoga.network.request.TransactionRequest
import com.globasure.giftoga.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class TransactionsFragViewModel @ViewModelInject constructor(
    private val getAllTransactionUseCase: GetAllTransactionUseCase
) : BaseViewModel<TransactionsFragView>() {

    fun getAllTransactionHistory(pageNumber: Int, pageItem: Int) {
        viewModelScope.launch {
            val transactionRequest = TransactionRequest(page = pageNumber, perPage = pageItem)
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
                getView()?.getAllTransactionSuccess(result.response.data.transaction, result.response.data.meta)
            }
            is GetAllTransactionUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.getAllTransactionSuccess(emptyList(), null)
            }
        }
    }
}