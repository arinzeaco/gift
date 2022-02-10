package com.globasure.giftoga.ui.screen.bulk_purchase

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.ChargeCardUseCase
import com.globasure.giftoga.domain.usecase.PurchasesBulkUseCase
import com.globasure.giftoga.domain.usecase.ValidateBulkUseCase
import com.globasure.giftoga.network.request.ChargeCardRequest
import com.globasure.giftoga.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import timber.log.Timber

@HiltViewModel
class BulkPurchaseViewModel @Inject constructor(
    private val chargeCardUseCase: ChargeCardUseCase,
    private val purchasesBulkUseCase: PurchasesBulkUseCase,
    private val validateBulkUseCase: ValidateBulkUseCase
) : BaseViewModel<BulkPurchaseView>() {

    fun chargeCard(chargeCardRequest: ChargeCardRequest) {
        handleChargeCardResult(ChargeCardUseCase.Result.Loading)

        viewModelScope.launch {
//            handleChargeCardResult(chargeCardUseCase.execute(request, chargeCardRequest))
        }
    }

    fun purchasebulk(payment_token: String) {
        handlePurchaseResult(PurchasesBulkUseCase.Result.Loading)

        viewModelScope.launch {
            handlePurchaseResult(purchasesBulkUseCase.execute(payment_token))
        }
    }

    fun validateBulkFile(merchant: String, excelFile: MultipartBody.Part) {
        handleBulkFileResult(ValidateBulkUseCase.Result.Loading)

        viewModelScope.launch {
            handleBulkFileResult(validateBulkUseCase.execute(merchant, excelFile))
        }
    }

    private fun handleChargeCardResult(result: ChargeCardUseCase.Result) {
        when (result) {
            is ChargeCardUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is ChargeCardUseCase.Result.Success -> {
//                purchasebulk(result.response.data.paymentToken)
            }
            is ChargeCardUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
//                val errorResponse = gson.fromJson(result.throwable.message, ErrorResponse::class.java)
//                AppUtils().failed_dialog(context,errorResponse.data.errors.message)
            }
        }
    }

    private fun handlePurchaseResult(result: PurchasesBulkUseCase.Result) {
        when (result) {
            is PurchasesBulkUseCase.Result.Loading -> {

            }
            is PurchasesBulkUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
//                getView()?.fundSuccess(result.response.message)
            }
            is PurchasesBulkUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    private fun handleBulkFileResult(result: ValidateBulkUseCase.Result) {
        when (result) {
            is ValidateBulkUseCase.Result.Loading -> {

            }
            is ValidateBulkUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                getView()?.validateBulkFileSuccess()
            }
            is ValidateBulkUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }
}