package com.globasure.giftoga.ui.screen.main_fragment.my_cards

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.GetMyCardUseCase
import com.globasure.giftoga.domain.usecase.SendMyCardUseCase
import com.globasure.giftoga.network.request.MyGiftCardRequest
import com.globasure.giftoga.network.request.SendGiftCardRequest
import com.globasure.giftoga.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class MyCardsFragViewModel @Inject constructor(
    private val getMyCardUseCase: GetMyCardUseCase,
    private val sendMyCardUseCase: SendMyCardUseCase
) : BaseViewModel<MyCardsFragView>() {

    fun getMyCards(request: MyGiftCardRequest) {
        viewModelScope.launch {
            handleGetMyCards(getMyCardUseCase.execute(request))
        }
    }

    fun sendToFriend(request: SendGiftCardRequest) {
        handleSendMyCard(SendMyCardUseCase.Result.Loading)

        viewModelScope.launch {
            handleSendMyCard(sendMyCardUseCase.execute(request))
        }
    }

    private fun handleGetMyCards(result: GetMyCardUseCase.Result) {
        when (result) {
            is GetMyCardUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is GetMyCardUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.getMyGiftCards(result.response.data.name)
            }
            is GetMyCardUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.getMyGiftCards(arrayListOf())
            }
        }
    }

    private fun handleSendMyCard(result: SendMyCardUseCase.Result) {
        when (result) {
            is SendMyCardUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is SendMyCardUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.sendMyGiftCardSuccess(result.response.message + " to " + result.response.data.friendName)
            }
            is SendMyCardUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }
}
