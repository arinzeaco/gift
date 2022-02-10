package com.globasure.giftoga.network.remote

import com.globasure.giftoga.network.SafeApiRequest
import com.globasure.giftoga.network.request.AllowDailyTransferRequest
import com.globasure.giftoga.network.request.BuyGiftcardRequest
import com.globasure.giftoga.network.request.CardOtpRequest
import com.globasure.giftoga.network.request.CardPinRequest
import com.globasure.giftoga.network.request.ChargeCardRequest
import com.globasure.giftoga.network.request.ChargeFeesRequest
import com.globasure.giftoga.network.request.ChargeGiftCardRequest
import com.globasure.giftoga.network.request.ChargeWalletRequest
import com.globasure.giftoga.network.request.GetLinkForBulkRequest
import com.globasure.giftoga.network.request.GetLinkForPaymentRequest
import com.globasure.giftoga.network.request.GetLinkForSalesRequest
import com.globasure.giftoga.network.request.LoginRequest
import com.globasure.giftoga.network.request.MerchantsRequest
import com.globasure.giftoga.network.request.MyGiftCardRequest
import com.globasure.giftoga.network.request.OpenCaseRequest
import com.globasure.giftoga.network.request.ResetPasswordRequest
import com.globasure.giftoga.network.request.SearchAllCasesRequest
import com.globasure.giftoga.network.request.SendGiftCardRequest
import com.globasure.giftoga.network.request.SignUpOtherRequest
import com.globasure.giftoga.network.request.SignUpPersonalRequest
import com.globasure.giftoga.network.request.TransactionRequest
import com.globasure.giftoga.network.request.TransferCommissionRequest
import com.globasure.giftoga.network.request.UpdateAddressRequest
import com.globasure.giftoga.network.request.UpdateNotificationRequest
import com.globasure.giftoga.network.request.UpdateProfileRequest
import com.globasure.giftoga.network.request.ValidateBankAccountRequest
import com.globasure.giftoga.network.request.ValidateTokenRequest
import com.globasure.giftoga.network.response.AddCardResponse
import com.globasure.giftoga.network.response.AllCasesResponse
import com.globasure.giftoga.network.response.AllReplyResponse
import com.globasure.giftoga.network.response.BankListResponse
import com.globasure.giftoga.network.response.BuyGiftCardResponse
import com.globasure.giftoga.network.response.CardOtpResponse
import com.globasure.giftoga.network.response.CardPinResponse
import com.globasure.giftoga.network.response.CaseDetailResponse
import com.globasure.giftoga.network.response.CategoriesResponse
import com.globasure.giftoga.network.response.CategoryResponse
import com.globasure.giftoga.network.response.ChargeBankTransferResponse
import com.globasure.giftoga.network.response.ChargeCardResponse
import com.globasure.giftoga.network.response.ChargeFeesResponse
import com.globasure.giftoga.network.response.ChargePayPalResponse
import com.globasure.giftoga.network.response.ChargeWalletResponse
import com.globasure.giftoga.network.response.CloseCaseResponse
import com.globasure.giftoga.network.response.CountryListResponse
import com.globasure.giftoga.network.response.DepositResponse
import com.globasure.giftoga.network.response.ExportTransactionResponse
import com.globasure.giftoga.network.response.GiftcardDetailResponse
import com.globasure.giftoga.network.response.ListCardResponse
import com.globasure.giftoga.network.response.LoginResponse
import com.globasure.giftoga.network.response.MerchantDetailResponse
import com.globasure.giftoga.network.response.MerchantsResponse
import com.globasure.giftoga.network.response.MyGiftCardResponse
import com.globasure.giftoga.network.response.OpenCaseResponse
import com.globasure.giftoga.network.response.PayLinkResponse
import com.globasure.giftoga.network.response.ProfileResponse
import com.globasure.giftoga.network.response.PurchaseBulkResponse
import com.globasure.giftoga.network.response.RedeemGiftCardResponse
import com.globasure.giftoga.network.response.RemoveCardResponse
import com.globasure.giftoga.network.response.ReplyCaseResponse
import com.globasure.giftoga.network.response.ReplyDetailResponse
import com.globasure.giftoga.network.response.ResetTokenResponse
import com.globasure.giftoga.network.response.SendGiftCardResponse
import com.globasure.giftoga.network.response.SignUpResponse
import com.globasure.giftoga.network.response.StateListResponse
import com.globasure.giftoga.network.response.TransactionResponse
import com.globasure.giftoga.network.response.TransferCommissionResponse
import com.globasure.giftoga.network.response.ValidateBankAccountResponse
import com.globasure.giftoga.network.response.ValidateBulkFileResponse
import com.globasure.giftoga.network.response.ValidateTokenResponse
import com.globasure.giftoga.network.response.VerificationResponse
import com.globasure.giftoga.utils.ApiUtil
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ApiHelperImpl @Inject constructor(private val api: API) : ApiHelper, SafeApiRequest() {

    override suspend fun signUpPersonal(request: SignUpPersonalRequest): SignUpResponse {
        return apiRequest { api.signUp(request) }
    }

    override suspend fun signUpBusiness(request: SignUpOtherRequest): SignUpResponse {
        return apiRequest { api.signUpBusiness(request) }
    }

    override suspend fun signUpAgent(request: SignUpOtherRequest): SignUpResponse {
        return apiRequest { api.signUpAgent(request) }
    }

    override suspend fun signUpMerchant(request: SignUpOtherRequest): SignUpResponse {
        return apiRequest { api.signUpMerchant(request) }
    }

    override suspend fun login(request: LoginRequest): LoginResponse {
        return apiRequest { api.signIn(request) }
    }

    override suspend fun refreshToken(refresh_token: String): LoginResponse {
        return apiRequest {
            api.refreshToken(
                ApiUtil.createJsonRequestBody(Pair("refresh_token", refresh_token))
            )
        }
    }

    override suspend fun getAccountDetails(): ProfileResponse {
        return apiRequest { api.getAccountDetails() }
    }

    override suspend fun requestResetToken(email: String): ResetTokenResponse {
        return apiRequest {
            api.requestResetToken(
                ApiUtil.createJsonRequestBody(Pair("email", email))
            )
        }
    }

    override suspend fun validateToken(request: ValidateTokenRequest): ValidateTokenResponse {
        return apiRequest { api.validateToken(request) }
    }

    override suspend fun resetPassword(request: ResetPasswordRequest): ResetTokenResponse {
        return apiRequest { api.resetPassword(request) }
    }

    override suspend fun getCountryList(): CountryListResponse {
        return apiRequest { api.getCountryList() }
    }

    override suspend fun getStatesByCountry(country: String): StateListResponse {
        return apiRequest {
            api.getStatesByCountry(
                ApiUtil.createJsonRequestBody(Pair("country", country))
            )
        }
    }

    override suspend fun requestConfirmAccount(security_token: String): SignUpResponse {
        return apiRequest {
            api.requestConfirmAccount(
                ApiUtil.createJsonRequestBody(Pair("token", security_token))
            )
        }
    }

    override suspend fun updateProfile(request: UpdateProfileRequest): ProfileResponse {
        return apiRequest { api.updateProfile(request) }
    }

    override suspend fun updateAddress(request: UpdateAddressRequest): ProfileResponse {
        return apiRequest { api.updateAddress(request) }
    }

    override suspend fun updateAvatar(avatar: MultipartBody.Part?): ProfileResponse {
        return apiRequest { api.updateAvatar(avatar) }
    }

    override suspend fun updateNotification(request: UpdateNotificationRequest): ProfileResponse {
        return apiRequest { api.updateNotification(request) }
    }

    override suspend fun getBankList(): BankListResponse {
        return apiRequest { api.getBankList() }
    }

    override suspend fun validateBankAccount(request: ValidateBankAccountRequest): ValidateBankAccountResponse {
        return apiRequest { api.validateBankAccount(request) }
    }

    override suspend fun validateBankAccount(
        paymentMethod: String,
        bankCode: String,
        accountId: String
    ): ValidateBankAccountResponse {
        return apiRequest {
            api.validateBankAccount(
                ApiUtil.createJsonRequestBody(
                    Pair("payment_method", paymentMethod),
                    Pair("bank_code", bankCode),
                    Pair("account_id", accountId)
                )
            )
        }
    }

    override suspend fun allowDailyTransfer(request: AllowDailyTransferRequest): ProfileResponse {
        return apiRequest { api.allowDailyTransfer(request) }
    }

    override suspend fun verifyBusiness(
        document_file: MultipartBody.Part?, document_type: String, bvn: String, rc_number: String
    ): VerificationResponse {
        return apiRequest {
            api.verifyBusiness(
                document_file,
                document_type.toRequestBody("text/plain".toMediaTypeOrNull()),
                bvn.toRequestBody("text/plain".toMediaTypeOrNull()),
                rc_number.toRequestBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    override suspend fun verifyCustomer(
        document_file: MultipartBody.Part?, document_type: String, bvn: String
    ): VerificationResponse {
        return apiRequest {
            api.verifyCustomer(
                document_file,
                document_type.toRequestBody("text/plain".toMediaTypeOrNull()),
                bvn.toRequestBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    override suspend fun getVerificationStatus(): VerificationResponse {
        return apiRequest { api.getVerificationStatus() }
    }

    override suspend fun getPayLinkForSales(request: GetLinkForSalesRequest): PayLinkResponse {
        return apiRequest { api.getPayLinkForSales(request) }
    }

    override suspend fun getPayLinkForBulk(request: GetLinkForBulkRequest): PayLinkResponse {
        return apiRequest { api.getPayLinkForBulk(request) }
    }

//    override suspend fun getLinkForPayment(forPaymentRequest: GetLinkForPaymentRequest): PayLinkResponse {
//        TODO("Not yet implemented")
//    }

//    override suspend fun getPayLinkForCard(request: GetLinkForCardRequest): PayLinkResponse {
//        return apiRequest { api.getPayLinkForCard(request) }
//    }

    override suspend fun getLinkForPayment(forPaymentRequest: GetLinkForPaymentRequest): PayLinkResponse {
        return apiRequest { api.getLinkForPayment(forPaymentRequest) }
    }

    override suspend fun chargeWallet(request: ChargeWalletRequest): ChargeWalletResponse {
        return apiRequest { api.chargeWallet(request) }
    }

    override suspend fun chargeGiftCard(request: ChargeGiftCardRequest): ChargeWalletResponse {
        return apiRequest { api.chargeGiftCard(request) }
    }

    override suspend fun chargeCard(request: ChargeCardRequest): ChargeCardResponse {
        return apiRequest { api.chargeCard(request) }
    }

    override suspend fun chargeFees(request: ChargeFeesRequest): ChargeFeesResponse {
        return apiRequest { api.chargeFees(request) }
    }

    override suspend fun cardPin(request: CardPinRequest): CardPinResponse {
        return apiRequest { api.cardPin(request) }
    }


    override suspend fun cardOtp(request: CardOtpRequest): CardOtpResponse {
        return apiRequest { api.cardOtp(request) }
    }

    override suspend fun chargeBankTransfer(pay_link: String): ChargeBankTransferResponse {
        return apiRequest {
            api.chargeBankTransfer(
                ApiUtil.createJsonRequestBody(Pair("pay_link", pay_link))
            )
        }
    }

    override suspend fun confirmBankTransfer(reference: String): ChargeBankTransferResponse {
        return apiRequest {
            api.confirmBankTransfer(
                ApiUtil.createJsonRequestBody(Pair("reference", reference))
            )
        }
    }

    override suspend fun chargePayPal(pay_link: String): ChargePayPalResponse {
        return apiRequest {
            api.chargePayPalPayStack(
                ApiUtil.createJsonRequestBody(Pair("pay_link", pay_link))
            )
        }
    }

    override suspend fun confirmPayPal(reference: String, order_id: String): ChargePayPalResponse {
        return apiRequest {
            api.confirmPayPalPayStack(
                ApiUtil.createJsonRequestBody(Pair("reference", reference), Pair("order_id", order_id))
            )
        }
    }

    override suspend fun deposit(payment_token: String): DepositResponse {
        return apiRequest {
            api.deposit(
                ApiUtil.createJsonRequestBody(Pair("payment_token", payment_token))
            )
        }
    }

    override suspend fun getAllTransaction(request: TransactionRequest): TransactionResponse {
        return apiRequest { api.getAllTransaction(request) }
    }

    override suspend fun getTransactionDetail(reference: String): DepositResponse {
        return apiRequest {
            api.getTransactionDetail(
                ApiUtil.createJsonRequestBody(Pair("reference", reference))
            )
        }
    }

    override suspend fun exportTransaction(request: TransactionRequest): ExportTransactionResponse {
        return apiRequest { api.exportTransaction(request) }
    }

    override suspend fun transferCommission(request: TransferCommissionRequest): TransferCommissionResponse {
        return apiRequest { api.transferCommission(request) }
    }

    override suspend fun transferRedeem(request: TransferCommissionRequest): TransferCommissionResponse {
        return apiRequest { api.transferRedeem(request) }
    }

    override suspend fun getBanks(country: String): BankListResponse {
        return apiRequest {
            api.getBanks(
                ApiUtil.createJsonRequestBody(Pair("country", country))
            )
        }
    }

    override suspend fun getAllMerchants(request: MerchantsRequest): MerchantsResponse {
        return apiRequest { api.getAllMerchants(request) }
    }

    override suspend fun getMerchantDetail(merchant_id: String): MerchantDetailResponse {
        return apiRequest {
            api.getMerchantDetail(
                ApiUtil.createJsonRequestBody(Pair("merchant_id", merchant_id))
            )
        }
    }

    override suspend fun getMerchantByCategoryId(category_id: String): MerchantsResponse {
        return apiRequest {
            api.getMerchantByCategoryId(
                ApiUtil.createJsonRequestBody(Pair("category_id", category_id))
            )
        }
    }

    override suspend fun getAllCategories(): CategoriesResponse {
        return apiRequest { api.getAllCategories() }
    }

    override suspend fun getCategoryDetail(category_id: String): CategoryResponse {
        return apiRequest {
            api.getCategoryDetail(
                ApiUtil.createJsonRequestBody(Pair("category_id", category_id))
            )
        }
    }

    override suspend fun getGiftCardDetail(giftcard: String): GiftcardDetailResponse {
        return apiRequest {
            api.getGiftCardDetail(
                ApiUtil.createJsonRequestBody(Pair("giftcard", giftcard))
            )
        }
    }

    override suspend fun buyGiftCard(request: BuyGiftcardRequest): BuyGiftCardResponse {
        return apiRequest { api.buyGiftCard(request) }
    }

    override suspend fun redeemGiftCard(giftcard: String, amount: String): RedeemGiftCardResponse {
        return apiRequest {
            api.redeemGiftCard(
                ApiUtil.createJsonRequestBody(Pair("giftcard", giftcard), Pair("amount", amount))
            )
        }
    }

    override suspend fun getMyGiftCard(giftcard: MyGiftCardRequest): MyGiftCardResponse {
        return apiRequest { api.myGiftCard(giftcard) }
    }

    override suspend fun sendMyGiftCard(request: SendGiftCardRequest): SendGiftCardResponse {
        return apiRequest { api.sendMyGiftCard(request) }
    }


    override suspend fun validateBulkFile(
        merchant: String,
        excelFile: MultipartBody.Part?
    ): ValidateBulkFileResponse {
        return apiRequest { api.validateBulkFile(merchant.toRequestBody("text/plain".toMediaTypeOrNull()), excelFile) }
    }

    override suspend fun purchasesBulk(payment_token: String): PurchaseBulkResponse {
        return apiRequest {
            api.purchasesBulk(
                ApiUtil.createJsonRequestBody(Pair("payment_token", payment_token))
            )
        }
    }

    override suspend fun addBankCard(payment_token: String): AddCardResponse {
        return apiRequest {
            api.addBankCard(
                ApiUtil.createJsonRequestBody(Pair("payment_token", payment_token))
            )
        }
    }

    override suspend fun getListBankCards(order_by: String, page: Int, per_page: Int): ListCardResponse {
        return apiRequest {
            api.getListBankCards(
                ApiUtil.createJsonRequestBody(
                    Pair("order_by", order_by),
                    Pair("page", page),
                    Pair("per_page", per_page)
                )
            )
        }
    }

    override suspend fun deleteBankCard(card_token: String): RemoveCardResponse {
        return apiRequest {
            api.deleteBankCard(
                ApiUtil.createJsonRequestBody(Pair("card_token", card_token))
            )
        }
    }

    override suspend fun openCase(request: OpenCaseRequest): OpenCaseResponse {
        return apiRequest { api.openCase(request) }
    }

    override suspend fun getCaseDetail(case_id: String): CaseDetailResponse {
        return apiRequest {
            api.getCaseDetail(
                ApiUtil.createJsonRequestBody(Pair("case_id", case_id))
            )
        }
    }

    override suspend fun replyCase(case_id: String, comment: String): ReplyCaseResponse {
        return apiRequest {
            api.replyCase(
                ApiUtil.createJsonRequestBody(Pair("case_id", case_id), Pair("comment", comment))
            )
        }
    }

    override suspend fun getReplyDetail(case_id: String, comment_id: String): ReplyDetailResponse {
        return apiRequest {
            api.getReplayDetail(
                ApiUtil.createJsonRequestBody(
                    Pair("case_id", case_id),
                    Pair("comment_id", comment_id)
                )
            )
        }
    }

    override suspend fun getAllReply(case_id: String): AllReplyResponse {
        return apiRequest {
            api.getAllReply(
                ApiUtil.createJsonRequestBody(Pair("case_id", case_id))
            )
        }
    }

    override suspend fun closeCase(case_id: String): CloseCaseResponse {
        return apiRequest {
            api.closeCase(
                ApiUtil.createJsonRequestBody(Pair("case_id", case_id))
            )
        }
    }

    override suspend fun searchAllCases(request: SearchAllCasesRequest): AllCasesResponse {
        return apiRequest { api.searchAllCases(request) }
    }
}