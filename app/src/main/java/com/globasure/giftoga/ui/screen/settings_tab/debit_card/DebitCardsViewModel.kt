package com.globasure.giftoga.ui.screen.settings_tab.debit_card

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.DeleteCardUseCase
import com.globasure.giftoga.domain.usecase.ListCardUseCase
import com.globasure.giftoga.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class DebitCardsViewModel @Inject constructor(
    private val listCardUseCase: ListCardUseCase,
    private val deleteCardUseCase: DeleteCardUseCase
) : BaseViewModel<DebitCardsView>() {

    fun getAllCards(page: Int, per_page: Int) {
        viewModelScope.launch {
            handleCardsResult(listCardUseCase.execute("desc", page, per_page))
        }
    }

    private fun handleCardsResult(result: ListCardUseCase.Result) {
        when (result) {
            is ListCardUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is ListCardUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.loadCardsSuccess(result.response.data.list, result.response.data.metaData)
            }
            is ListCardUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                //getView()?.handleError(result.throwable)
                getView()?.loadCardsSuccess(emptyList(), null)
            }
        }
    }

    fun removeCard(cardToken: String) {
        viewModelScope.launch {
            handleRemoveCardResult(deleteCardUseCase.execute(cardToken))
        }
    }

    private fun handleRemoveCardResult(result: DeleteCardUseCase.Result) {
        when (result) {
            is DeleteCardUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is DeleteCardUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.deleteCardSuccess()
            }
            is DeleteCardUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }
}